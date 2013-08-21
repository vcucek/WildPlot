package mafi.core.render;

import javax.media.opengl.GL;
import mafi.common.util.IntegrateProperies;
import mafi.common.util.SpecialParameter;
import mafi.core.DrawContext;
import mafi.core.event.SelectEvent;
import mafi.core.jocl.CLContext;
import mafi.core.jocl.CLProgram;
import mafi.core.primitive.Quad;
import mafi.core.shaders.BasicShader;
import mafi.core.shaders.BasicShaderFactory;
import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_context;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;

/**
 *
 * @author vito
 */
public class Plot2D extends AbstractPlot{

	private BasicShader shader;
	private Quad screenQuad = Quad.screenQuad;
	private final int type;

	public static final int EXPLICIT = 1;
	public static final int IMPLICIT = 2;

	public Plot2D(String function, int type)
	{
		super(function, type);
		//create shader
		if(type == EXPLICIT){
			shader = BasicShaderFactory.Plot2DExplicit();
		}
		else if(type == IMPLICIT){
			shader = BasicShaderFactory.Plot2DImplicit();
		}
		this.type = type;
	}

	public int getType(){
		return this.type;
	}

	private String getGLSL(){
		String insert = this.function;
		if(function.equalsIgnoreCase("") || this.function == null){
			insert = "0.0";
		}

		String out = "";

		for(SpecialParameter sp : this.specialParametersList){
			out += "uniform float " + sp.name + ";\n";
		}

		if(this.type == EXPLICIT){
			out += "float function(float x){\n";
			out += "	float funcValue;\n";
			out += "	funcValue = "+ insert +";\n";
			out += "	return funcValue;\n";
			out += "}\n";
		}
		else if(this.type == IMPLICIT){
			String[] f = insert.split("=");
			out += "float function(float x, float y){\n";
			out += "	float funcValue;\n";
			out += "	funcValue = "+ f[1] +";\n";
			out += "\n";
			out += "	float hit = 0.0;\n";
			out += "	hit = step("+ f[0] +" , funcValue);\n";
			out += "	return hit;\n";
			out += "}\n";
		}
		return out;
	}

	public void preRender(DrawContext dc)
	{
	}

	public void pick(DrawContext dc)
	{
	}

	public void doRender(DrawContext dc)
	{
		GL gl = dc.getGL();

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		shader.setPreprocessorCode(getGLSL());

		shader.enable(dc);
		double[] doubleArray = new double[16];
		doubleArray = dc.getView().getModelViewProjection().getInverse().toArray(doubleArray, 0, false);
		float[] floatArray = shader.convertDoubleToFloat(doubleArray);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(shader.getProgram(), "viewProjInverse"), 1, false, floatArray,0);
		gl.glUniform1f(gl.glGetUniformLocation(shader.getProgram(), "pixelWidth"), (float)dc.getView().getPixelWidth(0));
		gl.glUniform1f(gl.glGetUniformLocation(shader.getProgram(), "pixelHeight"), (float)dc.getView().getPixelHeight(0));

		for(SpecialParameter sp : this.specialParametersList){
			gl.glUniform1f(gl.glGetUniformLocation(shader.getProgram(), String.valueOf(sp.name)), (float)sp.getValue());
		}

		dc.getView().setMatrixMode(gl, GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

//		gl.glActiveTexture(GL.GL_TEXTURE0);
//		this.randomTexture.bind(dc);
//		gl.glUniform1f(gl.glGetUniformLocation(shader.getProgram(), "randomTexture"), 0);

		this.screenQuad.drawQuad(gl);

		gl.glPopMatrix();

		gl.glDisable(GL.GL_BLEND);

		shader.disable(dc);
	}

	public void dispose(DrawContext dc)
	{
	}

	public boolean isPickEnabled()
	{
		return false;
	}

	public void selected(SelectEvent event)
	{
	}

	public String integrate(CLContext clContext, IntegrateProperies properies){

		int steps = properies.nSteps;
		int nIterCpu = 1000;
		if(steps<nIterCpu){
			nIterCpu = steps;
		}
		int nIterPerProcesor = steps/nIterCpu;

		String programSource =
        "__kernel void "+
        "plotIntegrate(__global const float *min,"+
        "			__global const float *max,"+
        "			__global const int *steps,"+
        "			__global float *out)"+
        "{"+
        "	int gid = get_global_id(0);"+
		"	float rangeX = ((max[0]-min[0])/(float)get_global_size(0));"+
		"	float stepX = rangeX/(float)steps[0];"+
		"	float startX = min[0] + ((float)gid * rangeX);"+
		"	float sumOut = 0.0;"+
		"	for(int i = 0; i<steps[0];i++){"+
		"		float x = startX + (stepX * (float)i);"+
		"		sumOut += ("+ this.getFunction() +") * stepX;"+
		"	}"+
        "	out[gid] = sumOut;"+
        "}";

		cl_context context = clContext.getContext();
		CLProgram program = clContext.compileProgramSource(programSource);

		float[] out = new float[nIterCpu];
		Pointer minP = Pointer.to(new float[]{(float)properies.min});
		Pointer maxP = Pointer.to(new float[]{(float)properies.max});
		Pointer stepsP = Pointer.to(new int[]{nIterPerProcesor});
		Pointer outP = Pointer.to(out);

		// Allocate the memory objects for the input- and output data
		cl_mem memObjects[] = new cl_mem[4];
		memObjects[0] = CL.clCreateBuffer(context,
			CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
			Sizeof.cl_float, minP, null);
		memObjects[1] = CL.clCreateBuffer(context,
			CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
			Sizeof.cl_float, maxP, null);
		memObjects[2] = CL.clCreateBuffer(context,
			CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
			Sizeof.cl_int, stepsP, null);
		memObjects[3] = CL.clCreateBuffer(context,
			CL.CL_MEM_WRITE_ONLY,
			Sizeof.cl_float * nIterCpu, null, null);

		cl_kernel kernel = program.getKernel("plotIntegrate");

		// Set the arguments for the kernel
        CL.clSetKernelArg(kernel, 0,
            Sizeof.cl_mem, Pointer.to(memObjects[0]));
        CL.clSetKernelArg(kernel, 1,
            Sizeof.cl_mem, Pointer.to(memObjects[1]));
        CL.clSetKernelArg(kernel, 2,
            Sizeof.cl_mem, Pointer.to(memObjects[2]));
		CL.clSetKernelArg(kernel, 3,
            Sizeof.cl_mem, Pointer.to(memObjects[3]));

		// Set the work-item dimensions
        long global_work_size[] = new long[]{nIterCpu};
        long local_work_size[] = new long[]{1};

		long startTime = System.currentTimeMillis();

		// Execute the kernel
        CL.clEnqueueNDRangeKernel(clContext.getCommandQueue(), kernel, 1, null,
            global_work_size, local_work_size, 0, null, null);

        // Read the output data
        CL.clEnqueueReadBuffer(clContext.getCommandQueue(), memObjects[3], CL.CL_TRUE, 0,
            Sizeof.cl_float * nIterCpu, outP, 0, null, null);

		float integral = 0;
		for(int i = 0; i<nIterCpu; i++){
			integral += out[i];
		}
		long calcTime = System.currentTimeMillis() - startTime;

		CL.clReleaseMemObject(memObjects[0]);
        CL.clReleaseMemObject(memObjects[1]);
        CL.clReleaseMemObject(memObjects[2]);
        CL.clReleaseMemObject(memObjects[3]);

		program.dispose();
		return ("Integral:\n"+
				"min:"+properies.min+"\n"+
				"max:"+properies.max+"\n"+
				"steps:"+steps+"\n"+
				"=" + integral+"\n"+
				"time:" +calcTime+"\n");
	}
}
