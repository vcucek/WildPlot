package si.wildplot.core;

import si.wildplot.core.view.View;
import si.wildplot.core.event.SelectListener;
import si.wildplot.core.render.Renderable;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.event.InputHandler;
import si.wildplot.core.input.AWTInputHandler;
import si.wildplot.core.jocl.BasicCLContext;
import si.wildplot.core.render.Model;
import si.wildplot.core.view.BasicView;

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
public class WindowGLCanvas extends GLCanvas implements Window{
	private static final long serialVersionUID = 1L;

	private WindowGLDrawable wd;

	private static final GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());

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

	public BasicCLContext getCLContext(){
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
