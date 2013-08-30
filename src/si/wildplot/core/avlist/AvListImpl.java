package si.wildplot.core.avlist;

import java.beans.PropertyChangeSupport;
import si.wildplot.common.util.Logging;

/*
 * (C) Copyright 2013 Vito Čuček.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * @author Vito Čuček <vito.cucek@xlab.si>
 */
public class AvListImpl implements AvList{

	private PropertyChangeSupport changeSupport = null;

    public AvListImpl()
    {
    }

    /**
     * Constructor enabling aggregation
     *
     * @param sourceBean The bean to be given as the soruce for any events.
     */
    public AvListImpl(Object sourceBean)
    {
        if (sourceBean != null)
            this.changeSupport = new PropertyChangeSupport(sourceBean);
    }

	synchronized public PropertyChangeSupport getChangeSupport(){
		if(changeSupport == null){
			changeSupport = new PropertyChangeSupport(this);
		}
		return changeSupport;
	}

    synchronized public void addPropertyChangeListener(String propertyName, java.beans.PropertyChangeListener listener)
    {
        if (propertyName == null)
        {
            String msg = Logging.getMessage("nullValue.PropertyNameIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (listener == null)
        {
            String msg = Logging.getMessage("nullValue.ListenerIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.getChangeSupport().addPropertyChangeListener(propertyName, listener);
    }

    synchronized public void removePropertyChangeListener(String propertyName,
        java.beans.PropertyChangeListener listener)
    {
        if (propertyName == null)
        {
            String msg = Logging.getMessage("nullValue.PropertyNameIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (listener == null)
        {
            String msg = Logging.getMessage("nullValue.ListenerIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.getChangeSupport().removePropertyChangeListener(propertyName, listener);
    }

    synchronized public void addPropertyChangeListener(java.beans.PropertyChangeListener listener)
    {
        if (listener == null)
        {
            String msg = Logging.getMessage("nullValue.ListenerIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.getChangeSupport().addPropertyChangeListener(listener);
    }

    synchronized public void removePropertyChangeListener(java.beans.PropertyChangeListener listener)
    {
        if (listener == null)
        {
            String msg = Logging.getMessage("nullValue.ListenerIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.getChangeSupport().removePropertyChangeListener(listener);
    }

    synchronized public void firePropertyChange(java.beans.PropertyChangeEvent propertyChangeEvent)
    {
        if (propertyChangeEvent == null)
        {
            String msg = Logging.getMessage("nullValue.PropertyChangeEventIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.getChangeSupport().firePropertyChange(propertyChangeEvent);
    }

    synchronized public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        if (propertyName == null)
        {
            String msg = Logging.getMessage("nullValue.PropertyNameIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.getChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
    }
}
