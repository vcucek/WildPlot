package mafi.core;

import mafi.core.view.View;
import mafi.core.render.Renderable;
import mafi.common.math.Vec4;
import mafi.core.event.InputHandler;
import mafi.core.event.SelectListener;
import mafi.core.jocl.CLContext;
import mafi.core.render.Model;

/**
 *
 * @author vito
 */
public interface Window
{
	public AbstractSceneController getSceneController();
	public CLContext getCLContext();

	public InputHandler getInputHandler();
	public void setInputHandler(InputHandler inputHandler);

	public void setView(View view);
	public View getView();

	public Model getModel();
	public void setModel(Model model);

	public void redraw();
	public void redrawNow();
	public void shutdown();

	public Vec4 getPosition();
	public Renderable getSelection();
	public void setSelection(Renderable renderable);

	public void addSelectListener(SelectListener listener);
	public void removeSelectListener(SelectListener listener);
}
