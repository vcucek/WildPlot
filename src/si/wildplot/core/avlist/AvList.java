package si.wildplot.core.avlist;

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
public interface AvList {

	/**
     * Adds a property change listener for the specified key.
     *
     * @param propertyName the key to associate the listener with.
     * @param listener     the listener to associate with the key.
     *
     * @throws IllegalArgumentException if either <code>propertyName</code> or <code>listener</code> is null
     * @see java.beans.PropertyChangeSupport
     */
    void addPropertyChangeListener(String propertyName, java.beans.PropertyChangeListener listener);

    /**
     * Removes a property change listener associated with the specified key.
     *
     * @param propertyName the key associated with the change listener.
     * @param listener     the listener to remove.
     *
     * @throws IllegalArgumentException if either <code>propertyName</code> or <code>listener</code> is null
     * @see java.beans.PropertyChangeSupport
     */
    void removePropertyChangeListener(String propertyName, java.beans.PropertyChangeListener listener);

    /**
     * Adds the specified all-property property change listener that will be called for all list changes.
     *
     * @param listener the listener to call.
     *
     * @throws IllegalArgumentException if <code>listener</code> is null
     * @see java.beans.PropertyChangeSupport
     */
    void addPropertyChangeListener(java.beans.PropertyChangeListener listener);

    /**
     * Removes the specified all-property property change listener.
     *
     * @param listener the listener to remove.
     *
     * @throws IllegalArgumentException if <code>listener</code> is null
     * @see java.beans.PropertyChangeSupport
     */
    void removePropertyChangeListener(java.beans.PropertyChangeListener listener);

    /**
     * Calls all property change listeners associated with the specified key. No listeners are called if
     * <code>odValue</code> and <code>newValue</code> are equal and non-null.
     *
     * @param propertyName the key
     * @param oldValue     the value associated with the key before the even causing the firing.
     * @param newValue     the new value associated with the key.
     *
     * @throws IllegalArgumentException if <code>propertyName</code> is null
     * @see java.beans.PropertyChangeSupport
     */
    void firePropertyChange(String propertyName, Object oldValue, Object newValue);

    /**
     * Calls all registered property change listeners with the specified property change event.
     *
     * @param propertyChangeEvent the event
     *
     * @throws IllegalArgumentException if <code>propertyChangeEvent</code> is null
     * @see java.beans.PropertyChangeSupport
     */
    void firePropertyChange(java.beans.PropertyChangeEvent propertyChangeEvent);
}
