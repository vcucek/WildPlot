package si.wildplot.core;

import si.wildplot.core.render.BasicModel;
import si.wildplot.core.view.View;
import si.wildplot.core.view.View2D;
import si.wildplot.core.view.ViewCombined3D2D;

/**
 *
 * @author vito
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
