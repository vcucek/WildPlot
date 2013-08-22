package si.wildplot.core;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author vito
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
