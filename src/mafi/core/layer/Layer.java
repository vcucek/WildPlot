package mafi.core.layer;

import java.util.ArrayList;
import mafi.core.MafiObject;
import mafi.core.render.Renderable;

/**
 *
 * @author vito
 */
public interface Layer extends MafiObject, Renderable{

	public String getName();

	public void addRenderable(Renderable r);
	public void removeRenderable(Renderable r);

	public void setPickEnabled(boolean enable);
	public void setPosition(int position);
	public int getPosition();

	public ArrayList<Renderable> getRenderables();
}
