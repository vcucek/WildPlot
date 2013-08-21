package mafi.core;

/**
 *
 * @author vito
 */
public class BasicSceneController extends AbstractSceneController{

	@Override
	public void doRepaint(DrawContext dc)
	{
		this.initializeFrame(dc);
		try
		{
			this.clear(dc);
			this.applyView(dc);
			this.preRender(dc);
			this.clear(dc);
//			this.pick(dc);
//			this.clear(dc);
			this.draw(dc);
		}
		finally
		{
			this.finalizeFrame(dc);
		}
	}
}
