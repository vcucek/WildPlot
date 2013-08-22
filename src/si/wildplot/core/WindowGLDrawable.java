package si.wildplot.core;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import si.wildplot.common.util.Logging;


/**
 * @author: Vito Cucek
 */
public class WindowGLDrawable extends WindowImpl implements GLEventListener {
	private static final long serialVersionUID = 1L;

	GLAutoDrawable drawable;

	public WindowGLDrawable(){
		AbstractSceneController sc = this.getSceneController();
        if (sc != null)
        {
            sc.addPropertyChangeListener(this);
        }
	}

	public void initDrawable(GLAutoDrawable glAutoDrawable)
    {
        if (glAutoDrawable == null)
        {
            String msg = Logging.getMessage("nullValue.DrawableIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        this.drawable = glAutoDrawable;
        this.drawable.setAutoSwapBufferMode(false);
        this.drawable.addGLEventListener(this);
    }

	@Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        if (propertyChangeEvent == null)
        {
            String msg = Logging.getMessage("nullValue.PropertyChangeEventIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        if (this.drawable != null)
            this.drawable.display(); // Queue a JOGL repaint request.
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));
        GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        ((Component) drawable).setMinimumSize(new Dimension(0, 0));
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

		int redraw = 0;
        try
        {
            AbstractSceneController sc = this.getSceneController();
            if (sc == null)
            {
                Logging.logger().severe("WorldWindowGLCanvas.ScnCntrllerNullOnRepaint");
                throw new IllegalStateException(Logging.getMessage("WorldWindowGLCanvas.ScnCntrllerNullOnRepaint"));
            }

			//repaint canvas
			redraw = sc.repaint();

			gl.glFlush();
			drawable.swapBuffers();
		}
		catch (Exception e)
        {
            Logging.logger().log(Level.SEVERE, Logging.getMessage(
                "WorldWindowGLCanvas.ExceptionAttemptingRepaintWorldWindow"), e);
        }
		finally{
			if(redraw > 0){
				redraw();
			}
		}
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
//		display(drawable);
    }

	@Override
    public void redraw()
    {
        if (this.drawable != null)
            this.drawable.display();
    }

	@Override
    public void redrawNow()
    {
        if (this.drawable != null)
            this.drawable.display();
    }

	public void dispose(GLAutoDrawable glad) {
	}
}

