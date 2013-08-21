package mafi.core.render;

import javax.media.opengl.GL;
import mafi.core.DrawContext;
import mafi.core.MafiObjectImpl;
import mafi.core.event.SelectEvent;

/**
 *
 * @author vito
 */
public class TestSample extends MafiObjectImpl implements Renderable{

	public void preRender(DrawContext dc)
	{
	}

	public void pick(DrawContext dc)
	{
	}

	public void render(DrawContext dc)
	{
		GL gl = dc.getGL();
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
