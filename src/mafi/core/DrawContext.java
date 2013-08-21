package mafi.core;

import mafi.core.view.View;
import mafi.core.render.Renderable;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import mafi.core.jocl.CLContext;
import mafi.core.render.Model;


/**
 *
 * @author vito
 */
public interface DrawContext extends MafiObject{

	public void initialize(GLContext glContext);
	public void requestRedraw();

	public GL getGL();
	public GLU getGLU();
	public GLContext getGLContext();

	public CLContext getCLContext();
	public void setCLContext(CLContext clContext);

	public void addRenderable(Renderable r);
	public ArrayList<Renderable> getRenderables();
	public void removeRenderable(Renderable r);

	public View getView();
	public void setView(View view);

	public Model getModel();
	public void setModel(Model model);

	public int getDrawableWidth();
	public int getDrawableHeight();

	public void setDrawableWidth(int width);
	public void setDrawableHeight(int height);

	public boolean isPickingEnabled();
	public void setPickingEnabled(boolean enable);
}
