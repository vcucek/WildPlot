package mafi.core.input;

import java.awt.Point;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import mafi.core.Window;
import mafi.core.view.View;

/**
 *
 * @author vito
 */
public interface ViewInputHandler extends KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, FocusListener{

	public Window getWorldWindow();
	public void setWorldWindow(Window newWorldWindow);

	public void setView(View view);
	public View getView();

	public Point getMousePoint();
	public boolean isAnimate();

	void apply();

//	void apply();
//
//	void setEventSource(Window newWindow);
//    Window getEventSource();
//
//    void addSelectListener(SelectListener listener);
//    void removeSelectListener(SelectListener listener);
//    void addKeyListener(KeyListener listener);
//    void removeKeyListener(KeyListener listener);
//    void addMouseListener(MouseListener listener);
//    void removeMouseListener(MouseListener listener);
//    void addMouseMotionListener(MouseMotionListener listener);
//    void removeMouseMotionListener(MouseMotionListener listener);
//    void addMouseWheelListener(MouseWheelListener listener);
//    void removeMouseWheelListener(MouseWheelListener listener);
//    void dispose();
}
