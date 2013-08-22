package si.wildplot.core;

import si.wildplot.core.view.View;
import si.wildplot.core.render.Renderable;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.event.InputHandler;
import si.wildplot.core.event.SelectListener;
import si.wildplot.core.jocl.BasicCLContext;
import si.wildplot.core.render.Model;

/**
 *
 * @author vito
 */
public interface Window
{
	public AbstractSceneController getSceneController();
	public BasicCLContext getCLContext();

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
