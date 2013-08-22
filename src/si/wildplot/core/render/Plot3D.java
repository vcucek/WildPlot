package si.wildplot.core.render;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLContext;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Vec4;
import si.wildplot.common.util.IntegrateProperies;
import si.wildplot.common.util.SpecialParameter;
import si.wildplot.core.DrawContext;
import si.wildplot.core.event.SelectEvent;
import si.wildplot.core.jocl.BasicCLContext;
import si.wildplot.core.jocl.BasicCLProgram;
import si.wildplot.core.primitive.Quad;
import si.wildplot.core.primitive.Sector;
import si.wildplot.core.shaders.BasicShader;
import si.wildplot.core.shaders.BasicShaderFactory;
import si.wildplot.core.view.View;
import si.wildplot.core.view.ViewCombined3D2D;
import static com.jogamp.opencl.CLMemory.Mem.*;
import java.nio.FloatBuffer;

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

	private static boolean resetMinMaxZ = true;
	private static double maxZ = Double.MIN_VALUE;
	private static double minZ = Double.MAX_VALUE;

	//minMax calc
	CLKernel kernel = null;
	CLBuffer<FloatBuffer> outBuffer;

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
		GL2 gl = dc.getGL().getGL2();
		gl.glPushAttrib(GL2.GL_VIEWPORT_BIT 
				| GL2.GL_TRANSFORM_BIT
				| GL2.GL_TEXTURE_BIT
				| GL2.GL_HINT_BIT
				| GL2.GL_LINE_BIT
				| GL2.GL_POLYGON_BIT
				| GL2.GL_ENABLE_BIT
				| GL.GL_DEPTH_BUFFER_BIT
				| GL2.GL_CURRENT_BIT
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
			if(!gl.glIsFramebuffer(FboHeightMap[0])){
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
		//Vec4 minMaxV = new Vec4(0.0f,3.0f,0.0f,3.0f);
		minZ = minMaxV.x < minZ ? minMaxV.x : minZ;
		maxZ = minMaxV.y > maxZ ? minMaxV.y : maxZ;

		gl.glPopAttrib();
	}

	public void pick(DrawContext dc)
	{
	}

	public void dispose(DrawContext dc)
	{
		if(outBuffer != null){
			outBuffer.release();
		}
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
		gl.glBindBuffer(GL2.GL_PIXEL_UNPACK_BUFFER, 0);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_RGBA16F, resolution, resolution, 0, GL.GL_RGBA, GL.GL_FLOAT, null);
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
		gl.glBindBuffer(GL2.GL_PIXEL_UNPACK_BUFFER, 0);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, resolutionColor, resolutionColor, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		return colorMap;
	}

	private int[] buildFbo(DrawContext dc, int[] texture){
		GL2 gl = dc.getGL().getGL2();
		int[] fbo = new int[1];
		gl.glGenFramebuffers(1, fbo, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
		gl.glReadBuffer(GL.GL_COLOR_ATTACHMENT0);
		gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0);
		gl.glFramebufferTextureEXT(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, texture[0], 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		return fbo;
	}

	private int buildVertices(DrawContext dc, int resolution){
		GL2 gl = dc.getGL().getGL2();
		int mashList;
		mashList = gl.glGenLists(1);
		gl.glNewList(mashList, GL2.GL_COMPILE);

		gl.glBegin(GL2.GL_QUAD_STRIP);
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
		GL2 gl = dc.getGL().getGL2();

		resetMinMaxZ = true;

		gl.glPushAttrib(GL2.GL_VIEWPORT_BIT);
		gl.glViewport(0, 0, RESOLUTION_MASH, RESOLUTION_MASH);

		//render height and normalmap
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, this.FboHeightMap[0]);
		gl.glReadBuffer(GL2.GL_COLOR_ATTACHMENT0);
		gl.glDrawBuffer(GL2.GL_COLOR_ATTACHMENT0);

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
		float pWidth = (float)(vSector.maxX - vSector.minX)/(float)(RESOLUTION_MASH);
		float pHeight = (float)(vSector.maxY - vSector.minY)/(float)(RESOLUTION_MASH);

		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "pixelWidth"), pWidth);
		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "pixelHeight"), pHeight);

		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "res"), (float)this.RESOLUTION_MASH);

		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "minZ"), (float)minZ);
		gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), "maxZ"), (float)maxZ);

		for(SpecialParameter sp : this.specialParametersList){
			gl.glUniform1f(gl.glGetUniformLocation(heightMapShader.getProgram(), String.valueOf(sp.name)), (float)sp.getValue());
		}

		dc.getView().setMatrixMode(gl, GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		this.screenQuad.drawQuad(gl);
		gl.glPopMatrix();

		heightMapShader.disable(dc);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
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

	public String integrate(BasicCLContext context, IntegrateProperies properties)
	{
		return "Not supported yet.";
	}

	private void buildKernel(BasicCLContext clContext){

		String programSource =
        "__kernel void "+
        "sampleValues(float minX,"+
        "			  float maxX,"+
        "			  float minY,"+
        "			  float maxY,"+
        "			  global float *out)"+
        "{"+
        "	int res = (int)sqrt((float)get_global_size(0));"+
        "	int xInt = get_global_id(0)%(res);"+
        "	int yInt = (int)(get_global_id(0)/res);"+
		"	float stepX = ((maxX-minX)/(float)res);"+
		"	float stepY = ((maxY-minY)/(float)res);"+
		"	float x = minX + (stepX * (float)xInt);"+
		"	float y = minY + (stepY * (float)yInt);"+
		"	float fValue = ("+ this.getFunction() +");"+
        "	out[(yInt*res)+xInt] = fValue;"+
        "}";
		CLContext context = clContext.getContext();
		BasicCLProgram program = clContext.compileProgramSource(programSource);

		outBuffer = context.createFloatBuffer(RESOLUTION_MASH * RESOLUTION_MASH, WRITE_ONLY);
		kernel = program.getKernel("sampleValues");
	}

	private Vec4 calcMinMax(BasicCLContext clContext, Sector sector, int resolution){

		if(kernel == null){
			buildKernel(clContext);
		}

		kernel.setArg(0,(float)sector.minX)
			  .setArg(1,(float)sector.maxX)
			  .setArg(2,(float)sector.minY)
			  .setArg(3,(float)sector.maxY)
			  .setArg(4, outBuffer);

		// Set the work-item dimensions
		long local_work_size = Math.min(clContext.getDevice().getMaxWorkGroupSize(), 256);
        long global_work_size = clContext.globalWorkSizerRoundUp(local_work_size, resolution*resolution);

		//long local_work_size = 1;
        //long global_work_size = resolution*resolution;

		// Execute the kernel
		clContext.getCommandQueue().put1DRangeKernel(kernel, 0, global_work_size, local_work_size)
								   .putReadBuffer(outBuffer, true);

		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;

		for(int i = 0; i<(resolution*resolution); i++){
			float minMax = outBuffer.getBuffer().get(i);
			if(Float.isNaN(minMax) || Float.isInfinite(minMax)){
				continue;
			}
			min = Math.min(minMax, min);
			max = Math.max(minMax, max);
		}

		//outBuffer.getBuffer().clear();

		if(max-min < 0.00001){
			max = max + 0.00001f;
			min = min - 0.00001f;
		}

		return (new Vec4(min,max));
	}
}
