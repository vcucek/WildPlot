package si.wildplot.core.layer;

import java.util.ArrayList;
import si.wildplot.core.WildPlotObject;
import si.wildplot.core.render.Renderable;

/**
 *
 * @author vito
 */
public interface Layer extends WildPlotObject, Renderable{

	public String getName();

	public void addRenderable(Renderable r);
	public void removeRenderable(Renderable r);

	public void setPickEnabled(boolean enable);
	public void setPosition(int position);
	public int getPosition();

	public ArrayList<Renderable> getRenderables();
}
