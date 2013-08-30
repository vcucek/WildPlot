package si.wildplot.core;
import java.beans.PropertyChangeSupport;

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
public class PropertyChange {

	private static PropertyChange pc = null;
	private PropertyChangeSupport pcs = null;
	
	public static PropertyChangeSupport getInstance(){
		if(pc == null){
			pc = new PropertyChange();
			return pc.getPropertyChangeSupport();
		}
		else{
			return pc.getPropertyChangeSupport();
		}
	}

	public PropertyChange(){
		this.pcs = new PropertyChangeSupport(this);
	}

	public PropertyChangeSupport getPropertyChangeSupport(){
		return pcs;
	}
}
