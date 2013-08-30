package si.wildplot.core;

import si.wildplot.core.view.View;
import si.wildplot.core.render.Renderable;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
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
public interface DrawContext extends WildPlotObject{

	public void initialize(GLContext glContext);
	public void requestRedraw();

	public GL getGL();
	public GLU getGLU();
	public GLContext getGLContext();

	public BasicCLContext getCLContext();
	public void setCLContext(BasicCLContext clContext);

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
