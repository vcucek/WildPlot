package si.wildplot.core;

import si.wildplot.core.render.BasicModel;
import si.wildplot.core.view.View;
import si.wildplot.core.view.View2D;
import si.wildplot.core.view.ViewCombined3D2D;

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
public class SceneProvider {
	public final WindowGLCanvas window = new WindowGLCanvas();

	public final BasicModel model2D = new BasicModel();
	public final BasicModel model3D = new BasicModel();

	public final View view2D = new View2D();
	public final View view3D = new ViewCombined3D2D();

	private static SceneProvider instance = null;
	
	public SceneProvider(){
		
	}

	public static SceneProvider getInstance(){
		if(instance == null){
			instance = new SceneProvider();
		}
		return instance;
	}

	public void set3DScene(){
		window.setModel(model3D);
		window.setView(view3D);
	}

	public void set2DScene(){
		window.setModel(model2D);
		window.setView(view2D);
	}
}
