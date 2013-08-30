package si.wildplot.core.render;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.core.DrawContext;
import si.wildplot.core.WildPlotObjectImpl;
import si.wildplot.core.event.SelectEvent;

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
public class TestSample extends WildPlotObjectImpl implements Renderable{

	public void preRender(DrawContext dc)
	{
	}

	public void pick(DrawContext dc)
	{
	}

	public void render(DrawContext dc)
	{
		GL2 gl = dc.getGL().getGL2();
		gl.glBegin(GL.GL_TRIANGLES);
            gl.glColor3f(1.0f, 0.0f, 0.0f);    // Set the current drawing color to red
            gl.glVertex3f(0.0f, 1.0f, 0.0f);   // Top
            gl.glColor3f(0.0f, 1.0f, 0.0f);    // Set the current drawing color to green
            gl.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
            gl.glColor3f(0.0f, 0.0f, 1.0f);    // Set the current drawing color to blue
            gl.glVertex3f(1.0f, -1.0f, 0.0f);  // Bottom Right
        gl.glEnd();
	}

	public void dispose(DrawContext dc)
	{
	}

	public boolean isPickEnabled()
	{
		return false;
	}

	public void selected(SelectEvent event)
	{
		if(event.getPickedRenderable() == this){
			System.out.println("TEST sample selected!!!");
		}
		else{
			System.out.println("TEST sample NOT selected!!!");
		}
	}
}
