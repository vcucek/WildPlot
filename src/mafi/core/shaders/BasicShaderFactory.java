package mafi.core.shaders;

/**
 *
 * @author vito
 */
public class BasicShaderFactory {

	public static BasicShader Plot2DExplicit(){
		return (new BasicShader(new String[]{"Plot2Dexplicit.glsl"}));
	}
	public static BasicShader Plot2DImplicit(){
		return (new BasicShader(new String[]{"Plot2Dimplicit.glsl"}));
	}
	public static BasicShader HeightMap(){
		return (new BasicShader(new String[]{"HeightMap.glsl"}));
	}
	public static BasicShader Plot3DExplicit(){
		return (new BasicShader(new String[]{"Plot3Dexplicit.glsl"}));
	}
}
