package si.wildplot.core.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL3;
import si.wildplot.common.util.Logging;
import si.wildplot.core.DrawContext;

/**
 *
 * @author vito
 */
public class BasicShader {

	private String preProcessorCode = "";
	private boolean recompile = false;
	private boolean isCompiled = false;

	private int shaderID = 0;
	
	protected final String[] files;

	public BasicShader(String[] files){
		this.files = files;
	}

	private ArrayList<String> loadFile(String fileName)
	{
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader brv = new BufferedReader(new InputStreamReader(this
				.getClass().getResourceAsStream(fileName)));
		String line;
		try
		{
			while ((line = brv.readLine()) != null)
			{
				lines.add(line + "\n");
			}
		}
		catch (IOException ex)
		{
			Logging.logger().log(Level.SEVERE, Logging.getMessage(
                "AbstractShader.ExceptionAttemptinToReadShader"), ex);
		}
		return lines;
	}

	private void printShaderLog(GL2 gl, int shaderId, String[] files)
	{
		int[] logLength = new int[1];
		gl.glGetShaderiv(shaderId, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
		if (logLength[0] > 1) {
			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(shaderId, logLength[0], (int[])null, 0, log, 0);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("Error compiling the shader: " + new String(log));
			for(String s : files){
				System.out.println(s);
			}
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
	}

	private void printShaderLinkLog(GL2 gl, int programId, String[] files){
		int[] logLength = new int[1];
		gl.glGetObjectParameterivARB(programId, GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB, logLength, 0);
		if (logLength[0] > 1) {
			byte[] log = new byte[logLength[0]];
			gl.glGetInfoLogARB(programId, logLength[0], (int[])null, 0, log, 0);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("Error linking the shader program: " + new String(log));
			for(String s : files){
				System.out.println(s);
			}
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
	}

	private void printGLLog(GL gl){
		int er = 0;
		while(er == 0){
			int error = gl.glGetError();
			if(error != GL.GL_NO_ERROR)
			{
				System.out.println("-------------------------");
				System.out.println("Error in OpenGL!!!!!!!!!");
				System.out.println("Error num: " + error);
				System.out.println("-------------------------");
			}
			else{
				er = 1;
			}
		}
	}

	protected int loadProgram(GL2 gl, String[] files)
	{
		printGLLog(gl);

		int programId = gl.glCreateProgram();
		int vertexShaderId = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		int fragmentShaderId = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		gl.glAttachShader(programId, vertexShaderId);
		gl.glAttachShader(programId, fragmentShaderId);

		int n = files.length;
		boolean geo = false;

		ArrayList<String> srcCode = new ArrayList<String>();

		Pattern p = Pattern.compile(".*\\_GEOMETRY\\_.*");
		Matcher m;

		for(int i = 0; i < n; ++i) {
			ArrayList<String> file = loadFile(files[i]);
			for(String line : file){
				srcCode.add(line);
				m = p.matcher(line);
				while(m.find()){
					geo = true;
				}
			}
		}
		String[] srcCodeT;

		srcCode.add(0, this.preProcessorCode);
		srcCode.add(0, "#define _VERTEX_\n");
		srcCodeT = new String[srcCode.size()];
		srcCodeT = srcCode.toArray(srcCodeT);
		gl.glShaderSource(vertexShaderId, srcCode.size(), srcCodeT, null,0);
		gl.glCompileShader(vertexShaderId);
		printShaderLog(gl, vertexShaderId, files);

		if (geo) {
			int geometryShaderId = gl.glCreateShader(GL3.GL_GEOMETRY_SHADER);
			gl.glAttachShader(programId, geometryShaderId);
			srcCode.set(0, "#define _GEOMETRY_\n");
			srcCodeT = new String[srcCode.size()];
			gl.glShaderSource(geometryShaderId, srcCode.size(), srcCode.toArray(srcCodeT), null,0);
			gl.glCompileShader(geometryShaderId);
			printShaderLog(gl, geometryShaderId, files);
			gl.glProgramParameteriARB(programId, GL3.GL_GEOMETRY_INPUT_TYPE, GL.GL_TRIANGLES);
			gl.glProgramParameteriARB(programId, GL3.GL_GEOMETRY_OUTPUT_TYPE, GL.GL_TRIANGLE_STRIP);
			gl.glProgramParameteriARB(programId, GL3.GL_GEOMETRY_VERTICES_OUT, 3);
		}

		srcCode.set(0, "#define _FRAGMENT_\n");
		srcCodeT = new String[srcCode.size()];

		gl.glShaderSource(fragmentShaderId, srcCode.size(), srcCode.toArray(srcCodeT), null,0);
		gl.glCompileShader(fragmentShaderId);
		printShaderLog(gl, fragmentShaderId, files);

		gl.glLinkProgram(programId);
		printShaderLinkLog(gl, programId, files);
//		gl.glValidateProgram(programId);

		srcCode.clear();

		printGLLog(gl);

		return programId;
	}

	public int enable(DrawContext dc)
	{
		GL2 gl = dc.getGL().getGL2();
		if(!this.isCompiled){
			this.shaderID = loadProgram(gl, this.files);
			this.isCompiled = true;
			this.recompile = false;
		}
		else if(this.recompile){
			gl.glDeleteProgram(this.shaderID);
			this.shaderID = loadProgram(gl, this.files);
			this.recompile = false;
		}
		gl.glUseProgram(shaderID);
		return this.shaderID;
	}

	public void disable(DrawContext dc)
	{
		GL2 gl = dc.getGL().getGL2();
		gl.glUseProgram(0);
	}

	public void setPreprocessorCode(String string){
		if(this.preProcessorCode.compareTo(string) != 0){
			this.recompile = true;
		}
		this.preProcessorCode = string;
	}

	public int getProgram(){
		return this.shaderID;
	}

	public float[] convertDoubleToFloat(double[] input){
		float[] output = new float[input.length];
		for(int i=0; i<input.length; i++){
			output[i] = Double.valueOf(input[i]).floatValue();
		}
		return output;
	}
}
