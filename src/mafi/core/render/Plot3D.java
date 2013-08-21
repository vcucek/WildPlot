package mafi.core.render;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import java.awt.Point;
import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import mafi.common.math.Vec4;
import mafi.common.util.IntegrateProperies;
import mafi.common.util.SpecialParameter;
import mafi.common.util.ViewUtil;
import mafi.core.DrawContext;
import mafi.core.event.SelectEvent;
import mafi.core.jocl.CLContext;
import mafi.core.jocl.CLProgram;
import mafi.core.primitive.Quad;
import mafi.core.primitive.Sector;
import mafi.core.shaders.BasicShader;
import mafi.core.shaders.BasicShaderFactory;
import mafi.core.view.View;
import mafi.core.view.ViewCombined3D2D;
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
public class Plot3D extends AbstractPlot{

	private BasicShader heightMapShader;
	private BasicShader plot3Dshader;
	private Quad screenQuad = Quad.screenQuad;
	private TextRenderer textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 10));

	private boolean init = true;
	private static int[] FboHeightMap = new int[1];
	private static int[] HeightMap = new int[1];
	private static int[] FboColorMap = new int[1];
	private static int[] ColorMap = new int[1];
	private static int MashDisplayList;

	private static final int RESOLUTION_COLOR = 1024;
	private static final int RESOLUTION_MASH = 512;
	private static final int RESOLUTION_MINMAX = 64;

	private static boolean resetMinMaxZ = true;
	private static double maxZ = Double.MIN_VALUE;
	private static double minZ = Double.MAX_VALUE;

	//minMax calc
	cl_mem memObjects[] = new cl_mem[5];
	cl_kernel kernel = null;
	float[] minMax = new float[RESOLUTION_MINMAX * RESOLUTION_MINMAX];
	Pointer minMaxP = Pointer.to(minMax);

	public Plot3D(String function)
	{
		super(function, 2);
		this.heightMapShader = BasicShaderFactory.HeightMap();
		this.plot3Dshader = BasicShaderFactory.Plot3DExplicit();
	}

	public int getType()
	{
		return 0;
	}

	public void preRender(DrawContext dc)
	{
		//render coordsys
		GL gl = dc.getGL();
		gl.glPushAttrib(GL.GL_VIEWPORT_BIT 
				| GL.GL_TRANSFORM_BIT
				| GL.GL_TEXTURE_BIT
				| GL.GL_HINT_BIT
				| GL.GL_LINE_BIT
				| GL.GL_POLYGON_BIT
				| GL.GL_ENABLE_BIT
				| GL.GL_DEPTH_BUFFER_BIT
				| GL.GL_CURRENT_BIT
				| GL.GL_COLOR_BUFFER_BIT);

		if(this.init){
			if(!gl.glIsList(MashDisplayList)){
				MashDisplayList = buildVertices(dc, RESOLUTION_MASH);
			}
			if(!gl.glIsTexture(HeightMap[0])){
				HeightMap = buildTextureHeightMap(dc, RESOLUTION_MASH);
			}
			if(!gl.glIsTexture(ColorMap[0])){
				ColorMap = buildTextureColor(dc, RESOLUTION_COLOR);
			}
			if(!gl.glIsFramebufferEXT(FboHeightMap[0])){
				FboHeightMap = buildFbo(dc, HeightMap);
			}
			init = false;
		}

		if(resetMinMaxZ){
			minZ = Double.MAX_VALUE;
			maxZ = Double.MIN_VALUE;
			resetMinMaxZ = false;
		}

		Vec4 minMaxV = calcMinMax(dc.getCLContext(), dc.getView().getVisibleSector(), 64);
		minZ = minMaxV.x < minZ ? minMaxV.x : minZ;
		maxZ = minMaxV.y > maxZ ? minMaxV.y : maxZ;

		gl.glPopAttrib();
	}

	public void pick(DrawContext dc)
	{
	}

	public void dispose(DrawContext dc)
	{
		CL.clReleaseMemObject(memObjects[4]);
		System.out.println("CALLING DISPOSE ON PLOT3D!!!!!!!!!!!!!!!!!!!");
	}

	public boolean isPickEnabled()
	{
		return false;
	}

	public void selected(SelectEvent event)
	{
	}

	private int[] buildTextureHeightMap(DrawContext dc, int resolution){
		GL gl = dc.getGL();
		int[] heightMap = new int[1];
		gl.glActiveTexture(GL.GL_TEXTURE1);
		gl.glGenTextures(1, heightMap, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, heightMap[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		gl.glBindBuffer(GL.GL_PIXEL_UNPACK_BUFFER_ARB, 0);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA16F_ARB, resolution, resolution, 0, GL.GL_RGBA, GL.GL_FLOAT, null);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		return heightMap;
	}

	private int[] buildTextureColor(DrawContext dc, int resolutionColor){
		GL gl = dc.getGL();
		int[] colorMap = new int[1];
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glGenTextures(1, colorMap, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, colorMap[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		gl.glBindBuffer(GL.GL_PIXEL_UNPACK_BUFFER_ARB, 0);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, resolutionColor, resolutionColor, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		return colorMap;
	}

	private int[] buildFbo(DrawContext dc, int[] texture){
		GL gl = dc.getGL();
		int[] fbo = new int[1];
		gl.glGenFramebuffersEXT(1, fbo, 0);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);
		gl.glReadBuffer(GL.GL_COLOR_ATTACHMENT0_EXT);
		gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0_EXT);
		gl.glFramebufferTextureEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, texture[0], 0);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		return fbo;
	}

	private int buildVertices(DrawContext dc, int resolution){
		GL gl = dc.getGL();
		int mashList;
		mashList = gl.glGenLists(1);
		gl.glNewList(mashList, GL.GL_COMPILE);

		gl.glBegin(GL.GL_QUAD_STRIP);
		float step = 1.0f/(float)resolution;
		boolean makeEmpty = false;
		for(int i=0; i< resolution-1;i++){
			for(int j=0;j < resolution;j++){
				if(makeEmpty){
					makeEmpty = false;
					gl.glVertex2f(i*step,j*step);
					gl.glVertex2f(i*step,j*step);
				}
//				gl.glColor3d(Math.random(), Math.random(), Math.random());
				gl.glVertex2f(i*step,j*step);
				gl.glVertex2f((i+1)*step,j*step);
				if(j == resolution-1){
					makeEmpty = true;
					gl.glVertex2f((i+1)*step,j*step);
					gl.glVertex2f((i+1)*step,j*step);
				}
			}
		}
		gl.glEnd();
		gl.glEndList();
		return mashList;
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

		out += "float function(float x, float y){\n";
		out += "	float funcValue;\n";
		out += "	funcValue = "+ insert +";\n";
		out += "	return funcValue;\n";
		out += "}\n";

		return out;
	}

	@Override
	public void doRender(DrawContext dc)
	{
		GL gl = dc.getGL();

		resetMinMaxZ = true;

		gl.glPushAttrib(GL.GL_VIEWPORT_BIT);
		gl.glViewport(0, 0, this.RESOLUTION_MASH, this.RESOLUTION_MASH);

		//render height and normalmap
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, this.FboHeightMap[0]);
		gl.glReadBuffer(GL.GL_COLOR_ATTACHMENT0_EXT);
		gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0_EXT);

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		heightMapShader.setPreprocessorCode(getGLSL());

		heightMapShader.enable(dc);
		double[] doubleArray = new double[16];
		if(dc.getView() instanceof ViewCombined3D2D){
			View view2d = ((ViewCombined3D2D)dc.getView()).getView2d();
			doubleArray = view2d.getModelViewProjection().getInverse().toArray(doubleArray, 0, false);
		}
		else{
			doubleArray = dc.getView().getModelViewProjection().getInverse().toArray(doubleArray, 0, false);
		}
		float[] floatArray = heightMapShader.convertDoubleToFloat(doubleArray);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(heightMapShader.getProgram(), "viewProjInverse"), 1, false, floatArray,0);

		Sector vSector = dc.getView().getVisibleSector();
		float pWidth = (float)(vSector.maxX - vSector.minX)/(float)(this.RESOLUTION_MASH);
		float pHeight = (float)(vSector.maxY - vSector.minY)/(float)(this.RESOLUTION_MASH);

		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "pixelWidth"), pWidth);
		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "pixelHeight"), pHeight);

		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "res"), (float)this.RESOLUTION_MASH);

		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "minZ"), (float)minZ);
		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "maxZ"), (float)maxZ);

		for(SpecialParameter sp : this.specialParametersList){
			gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), String.valueOf(sp.name)), (float)sp.getValue());
		}

		dc.getView().setMatrixMode(gl, GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		this.screenQuad.drawQuad(gl);
		gl.glPopMatrix();

		heightMapShader.disable(dc);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		gl.glPopAttrib();

		//render 3d function
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.ColorMap[0]);
		gl.glActiveTexture(GL.GL_TEXTURE1);
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.HeightMap[0]);

		plot3Dshader.enable(dc);

		double[] mvpD = new double[16];
		mvpD = dc.getView().getModelViewProjection().toArray(mvpD, 0, false);
		float[] mvpF = plot3Dshader.convertDoubleToFloat(mvpD);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(plot3Dshader.getProgram(), "modelViewProj"), 1, false, mvpF,0);

		double[] mvD = new double[16];
		mvD = dc.getView().getModelViewMatrix().toArray(mvD, 0, false);
		float[] mvF = plot3Dshader.convertDoubleToFloat(mvD);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(plot3Dshader.getProgram(), "modelView"), 1, false, mvF,0);
		gl.glUniform1i(gl.glGetUniformLocation(plot3Dshader.getProgram(), "colorMap"), 0);
		gl.glUniform1i(gl.glGetUniformLocation(plot3Dshader.getProgram(), "heightMap"), 1);

		Vec4 ld = new Vec4(0.0, 0.0, 1.0);
		gl.glUniform3f(gl.glGetUniformLocation(plot3Dshader.getProgram(), "sunDirection"), (float)ld.x, (float)ld.y, (float)ld.z);

		gl.glCallList(MashDisplayList);

		plot3Dshader.disable(dc);
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glDisable(GL.GL_TEXTURE_2D);
//		gl.glEnable(GL.GL_BLEND);
//		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//
//		//////////////////render mouse coords and box/////////////////////
//		Point mousePoint = dc.getView().getInputHandler().getMousePoint();
//		FloatBuffer depth = BufferUtil.newFloatBuffer(1);
//		gl.glReadPixels(mousePoint.x, dc.getDrawableHeight() - mousePoint.y, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, depth);
////			Vec4 mouseVec = ViewUtil.calcCameraPosition(dc, new Vec4(mousePoint.x, mousePoint.y, depth.get(0)));
//		depth.clear();
//		Vec4 mouseWorldVec = ViewUtil.calcWorldPosition(dc,
//				dc.getView().getModelViewProjectionI(),
//				new Vec4(mousePoint.x, mousePoint.y, depth.get(0)));
//
//		//coordsys at mouse point
//		gl.glBegin(GL.GL_LINES);
//		gl.glColor4f(0.5f, 0.5f, 1.0f, 0.8f);
//		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y, mouseWorldVec.z + 0.05f);
//		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y, mouseWorldVec.z - 0.05f);
//		gl.glColor4f(0.5f, 1.0f, 0.5f, 0.8f);
//		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y + 0.05f, mouseWorldVec.z);
//		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y - 0.05f, mouseWorldVec.z);
//		gl.glColor4f(1.0f, 0.5f, 0.5f, 0.8f);
//		gl.glVertex3d(mouseWorldVec.x + 0.05f, mouseWorldVec.y, mouseWorldVec.z);
//		gl.glVertex3d(mouseWorldVec.x - 0.05f, mouseWorldVec.y, mouseWorldVec.z);
//		gl.glEnd();
//		//
//
//		//render planes
//		if(!dc.getView().getInputHandler().isAnimate() && mouseWorldVec.z >= -0.5 && mouseWorldVec.y <= 0.5){
//			gl.glBegin(GL.GL_POLYGON);
//			gl.glColor4f(0.5f, 0.5f, 1.0f, 0.3f);
//			gl.glVertex3f(-0.5f, -0.5f, (float)mouseWorldVec.z);
//			gl.glVertex3f(0.5f, -0.5f, (float)mouseWorldVec.z);
//			gl.glVertex3f(0.5f, 0.5f, (float)mouseWorldVec.z);
//			gl.glVertex3f(-0.5f, 0.5f, (float)mouseWorldVec.z);
//			gl.glEnd();
////				gl.glBegin(GL.GL_POLYGON);
////				gl.glColor4f(1.0f, 0.5f, 0.5f, 0.3f);
////				gl.glVertex3f((float)mouseWorldVec.x, -0.5f, -0.5f);
////				gl.glVertex3f((float)mouseWorldVec.x, 0.5f, -0.5f);
////				gl.glVertex3f((float)mouseWorldVec.x, 0.5f, 0.5f);
////				gl.glVertex3f((float)mouseWorldVec.x, -0.5f, 0.5f);
////				gl.glEnd();
////				gl.glBegin(GL.GL_POLYGON);
////				gl.glColor4f(0.5f, 1.0f, 0.5f, 0.3f);
////				gl.glVertex3f(-0.5f, (float)mouseWorldVec.y, -0.5f);
////				gl.glVertex3f(0.5f, (float)mouseWorldVec.y, -0.5f);
////				gl.glVertex3f(0.5f, (float)mouseWorldVec.y, 0.5f);
////				gl.glVertex3f(-0.5f, (float)mouseWorldVec.y, 0.5f);
////				gl.glEnd();
//		}
//
//		//render box
//		gl.glBegin(GL.GL_LINES);
//		gl.glColor4f(0.5f, 0.5f, 1.0f, 0.5f);
//		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
//		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
//		gl.glVertex3f(0.5f, 0.5f, -0.5f);
//		gl.glVertex3f(0.5f, 0.5f, 0.5f);
//		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
//		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
//		gl.glVertex3f(0.5f, -0.5f, 0.5f);
//		gl.glVertex3f(0.5f, -0.5f, -0.5f);
//
//		gl.glColor4f(1.0f, 0.5f, 0.5f, 0.5f);
//		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
//		gl.glVertex3f(0.5f, -0.5f, -0.5f);
//		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
//		gl.glVertex3f(0.5f, 0.5f, 0.5f);
//		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
//		gl.glVertex3f(0.5f, 0.5f, -0.5f);
//		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
//		gl.glVertex3f(0.5f, -0.5f, 0.5f);
//
//		gl.glColor4f(0.5f, 1.0f, 0.5f, 0.5f);
//		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
//		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
//		gl.glVertex3f(0.5f, -0.5f, 0.5f);
//		gl.glVertex3f(0.5f, 0.5f, 0.5f);
//		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
//		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
//		gl.glVertex3f(0.5f, -0.5f, -0.5f);
//		gl.glVertex3f(0.5f, 0.5f, -0.5f);
//		gl.glEnd();
//
//		gl.glEnable(GL.GL_TEXTURE_2D);
//
//		dc.getView().setMatrixMode(gl, GL.GL_PROJECTION);
//		gl.glPushMatrix();
//		gl.glLoadIdentity();
//		dc.getView().setMatrixMode(gl, GL.GL_MODELVIEW);
//		gl.glPushMatrix();
//		gl.glLoadIdentity();
//
//		gl.glActiveTexture(GL.GL_TEXTURE0);
//		textRenderer.beginRendering(dc.getDrawableWidth(), dc.getDrawableHeight(), true);
//		textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.8f);
//		textRenderer.draw("moj tekst", mousePoint.x, dc.getDrawableHeight() - mousePoint.y);
//		textRenderer.flush();
//		textRenderer.endRendering();
//
//		dc.getView().setMatrixMode(gl, GL.GL_PROJECTION);
//		gl.glPopMatrix();
//		dc.getView().setMatrixMode(gl, GL.GL_MODELVIEW);
//		gl.glPopMatrix();
//		//////////////////////////////////////////////////////////
//
//		gl.glDisable(GL.GL_BLEND);
//		gl.glDisable(GL.GL_TEXTURE_2D);

//			System.out.println("MPOSWorld: " + mouseWorldVec.x + " " + mouseWorldVec.y + " " + mouseWorldVec.z);
//			System.out.println("DEPTH: " + depth.get(0));

		/////////test///////
//			gl.glEnable(GL.GL_BLEND);
//			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//
//			gl.glBindTexture(GL.GL_TEXTURE_2D, this.heightMap[0]);
//
//			gl.glBegin(GL.GL_POLYGON);
//
//			gl.glTexCoord2f(0.0f, 0.0f);
//			gl.glVertex2f(-1.0f, -1.0f);
//			gl.glTexCoord2f(1.0f, 0.0f);
//			gl.glVertex2f(1.0f, -1.0f);
//			gl.glTexCoord2f(1.0f, 1.0f);
//			gl.glVertex2f(1.0f, 1.0f);
//			gl.glTexCoord2f(0.0f, 1.0f);
//			gl.glVertex2f(-1.0f, 1.0f);
//
//			gl.glEnd();
//			gl.glPopMatrix();
//			gl.glDisable(GL.GL_BLEND);
	}

	public String integrate(CLContext context, IntegrateProperies properties)
	{
		return "Not supported yet.";
	}

	private void buildKernel(CLContext clContext){

		String programSource =
        "__kernel void "+
        "sampleValues(__global const float *minX,"+
        "			__global const float *maxX,"+
        "			__global const float *minY,"+
        "			__global const float *maxY,"+
        "			__global float *out)"+
        "{"+
        "	int res = (int)sqrt((float)get_global_size(0));"+
        "	int xInt = get_global_id(0)%(res);"+
        "	int yInt = (int)(get_global_id(0)/res);"+
		"	float stepX = ((maxX[0]-minX[0])/(float)res);"+
		"	float stepY = ((maxY[0]-minY[0])/(float)res);"+
		"	float x = minX[0] + (stepX * (float)xInt);"+
		"	float y = minY[0] + (stepY * (float)yInt);"+
		"	float fValue = ("+ this.getFunction() +");"+
        "	out[(yInt*res)+xInt] = fValue;"+
        "}";
		cl_context context = clContext.getContext();
		CLProgram program = clContext.compileProgramSource(programSource);

		memObjects[4] = CL.clCreateBuffer(context,
			CL.CL_MEM_WRITE_ONLY,
			Sizeof.cl_float * RESOLUTION_MASH * RESOLUTION_MASH, null, null);

		kernel = program.getKernel("sampleValues");

		CL.clSetKernelArg(kernel, 4,
            Sizeof.cl_mem, Pointer.to(memObjects[4]));
	}

	private Vec4 calcMinMax(CLContext clContext, Sector sector, int resolution){

		if(kernel == null){
			buildKernel(clContext);
		}
		cl_context context = clContext.getContext();

		Pointer minX = Pointer.to(new float[]{(float)sector.minX});
		Pointer maxX = Pointer.to(new float[]{(float)sector.maxX});
		Pointer minY = Pointer.to(new float[]{(float)sector.minY});
		Pointer maxY = Pointer.to(new float[]{(float)sector.maxY});

		// Allocate the memory objects for the input- and output data
//		cl_mem memObjects[] = new cl_mem[5];
		memObjects[0] = CL.clCreateBuffer(context,
			CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
			Sizeof.cl_float, minX, null);
		memObjects[1] = CL.clCreateBuffer(context,
			CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
			Sizeof.cl_float, maxX, null);
		memObjects[2] = CL.clCreateBuffer(context,
			CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
			Sizeof.cl_float, minY, null);
		memObjects[3] = CL.clCreateBuffer(context,
			CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
			Sizeof.cl_float, maxY, null);


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
        long global_work_size[] = new long[]{resolution*resolution};
        long local_work_size[] = new long[]{1};

		// Execute the kernel
        CL.clEnqueueNDRangeKernel(clContext.getCommandQueue(), kernel, 1, null,
//            global_work_size, null, 0, null, null);
            global_work_size, local_work_size, 0, null, null);

        // Read the output data
        CL.clEnqueueReadBuffer(clContext.getCommandQueue(), memObjects[4], CL.CL_TRUE, 0,
            Sizeof.cl_float * resolution * resolution, minMaxP, 0, null, null);

		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;

		for(int i = 0; i<(resolution*resolution); i++){
			if(Float.isNaN(minMax[i]) || Float.isInfinite(minMax[i])){
				continue;
			}
			min = Math.min(minMax[i], min);
			max = Math.max(minMax[i], max);
		}

		CL.clReleaseMemObject(memObjects[0]);
        CL.clReleaseMemObject(memObjects[1]);
        CL.clReleaseMemObject(memObjects[2]);
        CL.clReleaseMemObject(memObjects[3]);

		if(max-min < 0.00001){
			max = max + 0.00001f;
			min = min - 0.00001f;
		}

		return (new Vec4(min,max));
	}
}
