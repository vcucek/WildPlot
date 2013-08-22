package si.wildplot.core.input;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.EventListenerList;
import si.wildplot.common.util.Logging;
import si.wildplot.core.WildPlotObjectImpl;
import si.wildplot.core.PCKey;
import si.wildplot.core.Window;
import si.wildplot.core.event.InputHandler;
import si.wildplot.core.event.SelectEvent;
import si.wildplot.core.event.SelectListener;

/**
 *
 * @author vito
 */
public class AWTInputHandler extends WildPlotObjectImpl implements InputHandler,
		FocusListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

	protected Window wd;
	private EventListenerList eventListeners = new EventListenerList();
	private SelectListener selectListener;

	public void setEventSource(Window newWindow)
	{
		if (newWindow != null && !(newWindow instanceof Component))
        {
            String message = Logging.getMessage("Awt.AWTInputHandler.EventSourceNotAComponent");
            Logging.logger().finer(message);
            throw new IllegalArgumentException(message);
        }

        if (newWindow == this.wd)
        {
            return;
        }

        this.eventListeners = new EventListenerList(); // make orphans of listener references

        if (this.wd != null)
        {
            Component c = (Component) this.wd;
            c.removeKeyListener(this);
            c.removeMouseMotionListener(this);
            c.removeMouseListener(this);
            c.removeMouseWheelListener(this);
            c.removeFocusListener(this);

            if (this.selectListener != null)
                this.wd.removeSelectListener(this.selectListener);

            if (this.wd.getSceneController() != null)
                this.wd.getSceneController().removePropertyChangeListener(PCKey.VIEW, this);
            }

        this.wd = newWindow;
        if (this.wd == null)
        {
            return;
        }

		this.wd.getView().getInputHandler().setWorldWindow(this.wd);

        Component c = (java.awt.Component) this.wd;
        c.addKeyListener(this);
        c.addMouseMotionListener(this);
        c.addMouseListener(this);
        c.addMouseWheelListener(this);
        c.addFocusListener(this);

        this.selectListener = new SelectListener()
        {
            public void selected(SelectEvent event)
            {
                if (event.getEventAction().equals(SelectEvent.ROLLOVER))
                {

                }
            }
        };
        this.wd.addSelectListener(this.selectListener);

        if (this.wd.getSceneController() != null)
            this.wd.getSceneController().addPropertyChangeListener(PCKey.VIEW, this);
	}

	public Window getEventSource()
	{
		return this.wd;
	}

	public void addSelectListener(SelectListener listener)
    {
        this.eventListeners.add(SelectListener.class, listener);
    }

    public void removeSelectListener(SelectListener listener)
    {
        this.eventListeners.remove(SelectListener.class, listener);
    }

    protected void callSelectListeners(SelectEvent event)
    {
        for (SelectListener listener : this.eventListeners.getListeners(SelectListener.class))
        {
            listener.selected(event);
        }
    }
	public void addKeyListener(KeyListener listener)
    {
        this.eventListeners.add(KeyListener.class, listener);
    }

    public void removeKeyListener(KeyListener listener)
    {
        this.eventListeners.remove(KeyListener.class, listener);
    }

    public void addMouseListener(MouseListener listener)
    {
        this.eventListeners.add(MouseListener.class, listener);
    }

    public void removeMouseListener(MouseListener listener)
    {
        this.eventListeners.remove(MouseListener.class, listener);
    }

    public void addMouseMotionListener(MouseMotionListener listener)
    {
        this.eventListeners.add(MouseMotionListener.class, listener);
    }

    public void removeMouseMotionListener(MouseMotionListener listener)
    {
        this.eventListeners.remove(MouseMotionListener.class, listener);
    }

    public void addMouseWheelListener(MouseWheelListener listener)
    {
        this.eventListeners.add(MouseWheelListener.class, listener);
    }

    public void removeMouseWheelListener(MouseWheelListener listener)
    {
        this.eventListeners.remove(MouseWheelListener.class, listener);
    }

    protected void callKeyPressedListeners(KeyEvent event)
    {
        for (KeyListener listener : this.eventListeners.getListeners(KeyListener.class))
        {
            listener.keyPressed(event);
        }
    }

    protected void callKeyReleasedListeners(KeyEvent event)
    {
        for (KeyListener listener : this.eventListeners.getListeners(KeyListener.class))
        {
            listener.keyReleased(event);
        }
    }

    protected void callKeyTypedListeners(KeyEvent event)
    {
        for (KeyListener listener : this.eventListeners.getListeners(KeyListener.class))
        {
            listener.keyTyped(event);
        }
    }

    protected void callMousePressedListeners(MouseEvent event)
    {
        for (MouseListener listener : this.eventListeners.getListeners(MouseListener.class))
        {
            listener.mousePressed(event);
        }
    }

    protected void callMouseReleasedListeners(MouseEvent event)
    {
        for (MouseListener listener : this.eventListeners.getListeners(MouseListener.class))
        {
            listener.mouseReleased(event);
        }
    }

    protected void callMouseClickedListeners(MouseEvent event)
    {
        for (MouseListener listener : this.eventListeners.getListeners(MouseListener.class))
        {
            listener.mouseClicked(event);
        }
    }

	protected void callMouseEnteredListeners(MouseEvent event)
    {
        for (MouseListener listener : this.eventListeners.getListeners(MouseListener.class))
        {
            listener.mouseEntered(event);
        }
    }

	protected void callMouseExitedListeners(MouseEvent event)
    {
        for (MouseListener listener : this.eventListeners.getListeners(MouseListener.class))
        {
            listener.mouseExited(event);
        }
    }

    protected void callMouseDraggedListeners(MouseEvent event)
    {
        for (MouseMotionListener listener : this.eventListeners.getListeners(MouseMotionListener.class))
        {
            listener.mouseDragged(event);
        }
    }

    protected void callMouseMovedListeners(MouseEvent event)
    {
        for (MouseMotionListener listener : this.eventListeners.getListeners(MouseMotionListener.class))
        {
            listener.mouseMoved(event);
        }
    }

    protected void callMouseWheelMovedListeners(MouseWheelEvent event)
    {
        for (MouseWheelListener listener : this.eventListeners.getListeners(MouseWheelListener.class))
        {
            listener.mouseWheelMoved(event);
		}
    }

	public void dispose()
	{
	}

	private boolean callViewListeners(InputEvent e){
		if(e.isConsumed()){
			return false;
		}

		if(this.wd == null){
			return false;
		}
		return true;
	}

	public void focusGained(FocusEvent e)
	{
	}

	public void focusLost(FocusEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
		this.callKeyTypedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().keyTyped(e);
		}
	}

	public void keyPressed(KeyEvent e)
	{
		this.callKeyPressedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e)
	{
		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().keyReleased(e);
		}
	}

	public void mouseClicked(MouseEvent e)
	{
		this.callMouseClickedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mouseClicked(e);
		}
	}

	public void mousePressed(MouseEvent e)
	{
		this.callMousePressedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		this.callMouseReleasedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mouseReleased(e);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		this.callMouseEnteredListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mouseEntered(e);
		}
	}

	public void mouseExited(MouseEvent e)
	{
		this.callMouseExitedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mouseExited(e);
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		this.callMouseDraggedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mouseDragged(e);
		}
	}

	public void mouseMoved(MouseEvent e)
	{
		this.callMouseMovedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mouseMoved(e);
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		this.callMouseWheelMovedListeners(e);

		if(callViewListeners(e)){
			this.wd.getView().getInputHandler().mouseWheelMoved(e);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event)
    {
        if (this.wd == null) // include this test to ensure any derived implementation performs it
        {
            return;
        }

        if (this.wd.getView() == null)
        {
            return;
        }

        if (event == null)
        {
            return;
        }

        if (event.getPropertyName().equals(PCKey.VIEW) &&
            (event.getSource() == this.wd.getSceneController()))
        {
            this.wd.getView().getInputHandler().setWorldWindow(this.wd);
        }

    }
}
