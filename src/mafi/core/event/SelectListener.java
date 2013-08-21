package mafi.core.event;

import java.util.EventListener;

/**
 *
 * @author vito
 */
public interface SelectListener extends EventListener{

	public void selected(SelectEvent event);

}
