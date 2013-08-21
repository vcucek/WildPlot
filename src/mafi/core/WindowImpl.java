package mafi.core;

import mafi.core.view.View;
import java.awt.EventQueue;
import javax.swing.event.EventListenerList;
import mafi.core.event.SelectListener;
import mafi.core.render.Renderable;
import mafi.common.math.Vec4;
import mafi.common.util.GLTaskService;
import mafi.core.event.InputHandler;
import mafi.core.event.SelectEvent;
import mafi.core.jocl.CLContext;
import mafi.core.render.Model;

/**
 * @author Vito Cucek
 */
public class WindowImpl extends MafiObjectImpl implements Window{

	private AbstractSceneController sceneController;
	private CLContext clContext;
	private InputHandler inputHandler;
	private final EventListenerList eventListeners = new EventListenerList();

    public WindowImpl() {
		this.sceneController = new BasicSceneController();
		this.clContext = new CLContext();
		this.sceneController.setCLContext(clContext);
    }

	public AbstractSceneController getSceneController(){
		return this.sceneController;
	}

	public CLContext getCLContext(){
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
