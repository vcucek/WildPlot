package si.wildplot.core.render;

import si.wildplot.core.DrawContext;
import si.wildplot.core.WildPlotObject;
import si.wildplot.core.event.SelectListener;

/**
 *
 * @author vito
 */
public interface Renderable extends WildPlotObject, SelectListener{

	public void preRender(DrawContext dc);
	public void pick(DrawContext dc);
	public void render(DrawContext dc);
	public void dispose(DrawContext dc);

	public boolean isPickEnabled();
}
