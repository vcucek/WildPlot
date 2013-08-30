package si.wildplot.core.shaders;

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
