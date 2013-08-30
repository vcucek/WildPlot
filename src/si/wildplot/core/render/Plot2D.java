package si.wildplot.core.render;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLContext;
import com.jogamp.opencl.CLKernel;
import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.util.IntegrateProperies;
import si.wildplot.common.util.SpecialParameter;
import si.wildplot.core.DrawContext;
import si.wildplot.core.event.SelectEvent;
import si.wildplot.core.jocl.BasicCLContext;
import si.wildplot.core.jocl.BasicCLProgram;
import si.wildplot.core.primitive.Quad;
import si.wildplot.core.shaders.BasicShader;
import si.wildplot.core.shaders.BasicShaderFactory;
import static com.jogamp.opencl.CLMemory.Mem.*;

/*
 * (C) Copyright 2013 Vito Čuček.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * @author Vito Čuček <vito.cucek@xlab.si>
 */
public class Plot2D extends AbstractPlot{

	private BasicShader shader;
	private Quad screenQuad = Quad.screenQuad;
	private final int type;


	public Plot2D(String function, int type)
	{
		super(function, type);
		//create shader
		if(type == Function.TYPE_2D_EXPLICIT){
			shader = BasicShaderFactory.Plot2DExplicit();
		}
		else if(type == Function.TYPE_2D_IMPLICIT){
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

		if(this.type == Function.TYPE_2D_EXPLICIT){
			out += "float function(float x){\n";
			out += "	float funcValue;\n";
			out += "	funcValue = "+ insert +";\n";
			out += "	return funcValue;\n";
			out += "}\n";
		}
		else if(this.type == Function.TYPE_2D_IMPLICIT){
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
		GL2 gl = dc.getGL().getGL2();

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

		dc.getView().setMatrixMode(gl, GL2.GL_MODELVIEW);
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

	public String integrate(BasicCLContext clContext, IntegrateProperies properies){

		int steps = properies.nSteps;
		int nIterCpu = 1000;
		if(steps<nIterCpu){
			nIterCpu = steps;
		}
		int nIterPerProcesor = steps/nIterCpu;

		String programSource =
        "__kernel void "+
        "plotIntegrate(float min,"+
        "			float max,"+
        "			int steps,"+
        "			global float *out)"+
        "{"+
        "	int gid = get_global_id(0);"+
		"	float rangeX = ((max-min)/(float)get_global_size(0));"+
		"	float stepX = rangeX/(float)steps;"+
		"	float startX = min + ((float)gid * rangeX);"+
		"	float sumOut = 0.0;"+
		"	for(int i = 0; i<steps;i++){"+
		"		float x = startX + (stepX * (float)i);"+
		"		sumOut += ("+ this.getFunction() +") * stepX;"+
		"	}"+
        "	out[gid] = sumOut;"+
        "}";

		CLContext context = clContext.getContext();
		BasicCLProgram program = clContext.compileProgramSource(programSource);

		CLBuffer<FloatBuffer> outBuffer = context.createFloatBuffer(nIterCpu, WRITE_ONLY);

		CLKernel kernel = program.getKernel("plotIntegrate");
		kernel.putArg((float)properies.min)
			  .putArg((float)properies.max)
			  .putArg(nIterPerProcesor)
			  .putArg(outBuffer);

		// Set the work-item dimensions
        long local_work_size = Math.min(clContext.getDevice().getMaxWorkGroupSize(), 256);
        long global_work_size = clContext.globalWorkSizerRoundUp(local_work_size, nIterCpu);

		long startTime = System.currentTimeMillis();

		clContext.getCommandQueue().put1DRangeKernel(kernel, 0, global_work_size, local_work_size)
								   .putReadBuffer(outBuffer, true);

		float integral = 0;
		for(int i = 0; i<nIterCpu; i++){
			integral += outBuffer.getBuffer().get(i);
		}
		long calcTime = System.currentTimeMillis() - startTime;

		outBuffer.release();
		program.dispose();

		return ("Integral:\n"+
				"min:"+properies.min+"\n"+
				"max:"+properies.max+"\n"+
				"steps:"+steps+"\n"+
				"=" + integral+"\n"+
				"time:" +calcTime+"\n");
	}
}
