package si.wildplot.core;

import si.wildplot.common.util.Logging;
import si.wildplot.core.avlist.AvListImpl;

/**
 *
 * @author vito
 */
public class WildPlotObjectImpl extends AvListImpl implements java.beans.PropertyChangeListener{

	public WildPlotObjectImpl()
	{
	}

	public WildPlotObjectImpl(Object source)
	{
		super(source);
	}

	public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent)
    {
        if (propertyChangeEvent == null)
        {
            String msg = Logging.getMessage("nullValue.PropertyChangeEventIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        // Notify all *my* listeners of the change that I caught
        this.getChangeSupport().firePropertyChange(propertyChangeEvent);
    }
}
