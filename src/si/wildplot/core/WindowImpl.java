package si.wildplot.core;

import si.wildplot.core.view.View;
import java.awt.EventQueue;
import javax.swing.event.EventListenerList;
import si.wildplot.core.event.SelectListener;
import si.wildplot.core.render.Renderable;
import si.wildplot.common.math.Vec4;
import si.wildplot.common.util.GLTaskService;
import si.wildplot.core.event.InputHandler;
import si.wildplot.core.event.SelectEvent;
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
public class WindowImpl extends WildPlotObjectImpl implements Window{

	private AbstractSceneController sceneController;
	private BasicCLContext clContext;
	private InputHandler inputHandler;
	private final EventListenerList eventListeners = new EventListenerList();

    public WindowImpl() {
		this.sceneController = new BasicSceneController();
		this.clContext = new BasicCLContext();
		this.sceneController.setCLContext(clContext);
    }

	public AbstractSceneController getSceneController(){
		return this.sceneController;
	}

	public BasicCLContext getCLContext(){
		return clContext;
	}

	public void shutdown(){

		if(this.getModel() != null){
			this.getModel().dispose();
		}

		while(GLTaskService.getInstance().hasDisposeTasks()){
			redrawNow();
		}

		this.clContext.dispose();
	}

	public void setInputHandler(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	public InputHandler getInputHandler(){
		return this.inputHandler;
	}

	public void setView(View view){
		if(this.sceneController != null){
			this.sceneController.setView(view);
		}
	}

	public View getView(){
		return this.sceneController != null ? this.sceneController.getView() : null;
	}

	public Model getModel()
	{
		return this.sceneController != null ? this.sceneController.getModel() : null;
	}

	public void setModel(Model model)
	{
		if(this.sceneController != null){
			addSelectListener(model);
			getSceneController().setModel(model);
		}
	}

	public void redraw()
	{
	}

	public void redrawNow()
	{
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
		callSelectListeners(new SelectEvent(this, renderable));
	}

	public void addSelectListener(SelectListener listener)
    {
        this.eventListeners.add(SelectListener.class, listener);
    }

    public void removeSelectListener(SelectListener listener)
    {
        this.eventListeners.remove(SelectListener.class, listener);
    }

	public void callSelectListeners(final SelectEvent event)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                for (SelectListener listener : eventListeners.getListeners(SelectListener.class))
                {
                    listener.selected(event);
                }
            }
        });
    }
}
