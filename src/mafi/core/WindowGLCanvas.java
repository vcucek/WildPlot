package mafi.core;

import mafi.core.view.View;
import mafi.core.event.SelectEvent;
import mafi.core.event.SelectListener;
import mafi.core.render.Renderable;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import mafi.common.math.Vec4;
import mafi.core.event.InputHandler;
import mafi.core.input.AWTInputHandler;
import mafi.core.jocl.CLContext;
import mafi.core.render.Model;
import mafi.core.view.BasicView;

/**
 *
 * @author vito
 */
public class WindowGLCanvas extends GLCanvas implements Window{
	private static final long serialVersionUID = 1L;

	private WindowGLDrawable wd;

	private static final GLCapabilities caps = new GLCapabilities();

	static
	{
		caps.setHardwareAccelerated(true);
		caps.setDoubleBuffered(true);
	
		caps.setStencilBits(4);
		caps.setAlphaBits(8);
		caps.setRedBits(8);
		caps.setGreenBits(8);
		caps.setBlueBits(8);
		caps.setDepthBits(24);
	}

	public WindowGLCanvas(){
		super(caps);
		this.setAutoSwapBufferMode(false);
		this.wd = new WindowGLDrawable();
		this.init();
	}

	private void init(){
		this.wd.initDrawable(this);
		this.setView(new BasicView());
		this.setInputHandler(new AWTInputHandler());
		this.addGLEventListener((GLEventListener) wd);
        this.setMinimumSize(new Dimension());
	}

	public AbstractSceneController getSceneController()
	{
		return this.wd.getSceneController();
	}

	public CLContext getCLContext(){
		return this.wd.getCLContext();
	}

	public InputHandler getInputHandler()
	{
		return this.wd.getInputHandler();
	}

	public void setInputHandler(InputHandler inputHandler)
	{
		if (this.wd.getInputHandler() != null)
		{
			this.wd.getInputHandler().setEventSource(null); // remove this window as a source of events
		}
		this.wd.setInputHandler(inputHandler != null ? inputHandler : new AWTInputHandler());
		if (inputHandler != null)
		{
			inputHandler.setEventSource(this);
		}
	}

	public void setView(View view)
	{
		this.wd.setView(view);
	}

	public View getView()
	{
		return this.wd.getView();
	}

	public Model getModel()
	{
		return this.wd.getModel();
	}

	public void setModel(Model model)
	{
		this.wd.setModel(model);
	}

	public void redraw()
	{
		this.wd.redraw();
	}

	public void redrawNow()
	{
		this.wd.redrawNow();
	}

	public void shutdown()
	{
		this.wd.shutdown();
	}

	public Vec4 getPosition()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Renderable getSelection()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setSelection(Renderable renderable)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addSelectListener(SelectListener listener)
	{
		this.wd.getInputHandler().addSelectListener(listener);
		this.wd.addSelectListener(listener);
	}

	public void removeSelectListener(SelectListener listener)
	{
		this.wd.getInputHandler().addSelectListener(listener);
		this.wd.removeSelectListener(listener);
	}

	@Override
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener)
	{
		super.addPropertyChangeListener(listener);
		this.wd.addPropertyChangeListener(listener);
	}

	@Override
	public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		super.addPropertyChangeListener(propertyName, listener);
		this.wd.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener)
	{
		super.removePropertyChangeListener(listener);
		this.wd.removePropertyChangeListener(listener);
	}

	@Override
	public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		super.removePropertyChangeListener(listener);
		this.wd.removePropertyChangeListener(listener);
	}

	@Override
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void firePropertyChange(PropertyChangeEvent propertyChangeEvent)
	{
		this.wd.firePropertyChange(propertyChangeEvent);
	}
}
