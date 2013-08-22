package si.wildplot.core.event;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import si.wildplot.core.MafiObject;
import si.wildplot.core.Window;

/**
 *
 * @author vito
 */
public interface InputHandler extends MafiObject{

	void setEventSource(Window newWindow);
    Window getEventSource();

    void addSelectListener(SelectListener listener);
    void removeSelectListener(SelectListener listener);
    void addKeyListener(KeyListener listener);
    void removeKeyListener(KeyListener listener);
    void addMouseListener(MouseListener listener);
    void removeMouseListener(MouseListener listener);
    void addMouseMotionListener(MouseMotionListener listener);
    void removeMouseMotionListener(MouseMotionListener listener);
    void addMouseWheelListener(MouseWheelListener listener);
    void removeMouseWheelListener(MouseWheelListener listener);
    void dispose();

}
