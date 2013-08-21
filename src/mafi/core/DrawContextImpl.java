package mafi.core;

import mafi.core.view.View;
import mafi.core.render.Renderable;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;
import mafi.common.util.Logging;
import mafi.core.jocl.CLContext;
import mafi.core.render.Model;

/**
 *
 * @author vito
 */
public class DrawContextImpl extends MafiObjectImpl implements DrawContext{

	private GLContext glContext;
	private GLU glu = new GLU();
	private View view;
	private Model model;
	private CLContext clContext;

	private int drawableW;
	private int drawableH;

	private boolean isPickingEnabled = true;

	public static int redrawRequest = 10;

	public DrawContextImpl(){
	}

	public final void initialize(GLContext glContext)
	{
        if (glContext == null)
        {
            String message = Logging.getMessage("nullValue.GLContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        this.glContext = glContext;

		this.drawableW = this.getGLDrawable().getWidth();
		this.drawableH = this.getGLDrawable().getHeight();
	}

	public final void setGLContext(GLContext glContext)
    {
        if (glContext == null)
        {
            String message = Logging.getMessage("nullValue.GLContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        this.glContext = glContext;
    }

	public final GLContext getGLContext()
	{
		return this.glContext;
	}

	public CLContext getCLContext(){
		return this.clContext;
	}

	public void setCLContext(CLContext clContext){
		this.clContext = clContext;
	}

	public final GL getGL()
	{
		return this.glContext.getGL();
	}

	public final GLU getGLU()
	{
		return this.glu;
	}

	public final int getDrawableHeight()
    {
        return this.drawableH;
    }

    public final int getDrawableWidth()
    {
        return this.drawableW;
    }

	public void setDrawableWidth(int width)
	{
		this.drawableW = width;
	}
	public void setDrawableHeight(int height)
	{
		this.drawableH = height;
	}

    public final GLDrawable getGLDrawable()
    {
        return this.getGLContext().getGLDrawable();
    }

	public void addRenderable(Renderable r)
	{
		if(r != null){
			this.model.addRenderable(r);
		}
	}

	public ArrayList<Renderable> getRenderables()
	{
		return model.getRenderables();
	}

	public void removeRenderable(Renderable r)
	{
		if(r != null){
			model.getRenderables().remove(r);
		}
	}

	public View getView()
	{
		return this.view;
	}

	public void setView(View view)
	{
		this.view = view;
	}

	public Model getModel()
	{
		return this.model;
	}

	public void setModel(Model model)
	{
		this.model = model;
	}

	public void requestRedraw(){
		if(redrawRequest < 30)
			redrawRequest += 1;
	}

	public boolean isPickingEnabled()
	{
		return this.isPickingEnabled;
	}

	public void setPickingEnabled(boolean enable)
	{
		this.isPickingEnabled = enable;
	}
}
