package si.wildplot.core;

import si.wildplot.core.view.View;
import si.wildplot.core.render.Renderable;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;
import si.wildplot.common.util.Logging;
import si.wildplot.core.jocl.BasicCLContext;
import si.wildplot.core.render.Model;

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
public class DrawContextImpl extends WildPlotObjectImpl implements DrawContext{

	private GLContext glContext;
	private GLU glu = new GLU();
	private View view;
	private Model model;
	private BasicCLContext clContext;

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

	public BasicCLContext getCLContext(){
		return this.clContext;
	}

	public void setCLContext(BasicCLContext clContext){
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
