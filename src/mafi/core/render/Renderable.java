package mafi.core.render;

import mafi.core.DrawContext;
import mafi.core.MafiObject;
import mafi.core.event.SelectListener;

/**
 *
 * @author vito
 */
public interface Renderable extends MafiObject, SelectListener{

	public void preRender(DrawContext dc);
	public void pick(DrawContext dc);
	public void render(DrawContext dc);
	public void dispose(DrawContext dc);

	public boolean isPickEnabled();
}
