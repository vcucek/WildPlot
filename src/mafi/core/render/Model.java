package mafi.core.render;

import java.util.ArrayList;
import mafi.core.MafiObject;
import mafi.core.event.SelectEvent;
import mafi.core.event.SelectListener;
import mafi.core.layer.Layer;

/**
 *
 * @author vito
 */
public interface Model extends MafiObject, SelectListener{

	public void addLayer(String name, int position);
	public void addLayer(Layer layer);
	public void setCurrentLayer(String name);
	public void setCurrentLayer(Layer layer);
	public void pushCurrentLayer();
	public void popCurrentLayer();
	public void removeLayer(String name);
	public void removeLayer(Layer layer);

	public Layer getLayer(String name);

	public void addRenderable(Renderable r);
	public void removeRenderable(Renderable r);
	public ArrayList<Renderable> getRenderables();

	public void setCurrentSelection(Renderable r);
	public void setCurrentSelection(SelectEvent event);
	public Renderable getCurrentSelection();

	public Iterable<Layer> getLayers();

	public void dispose();
}
