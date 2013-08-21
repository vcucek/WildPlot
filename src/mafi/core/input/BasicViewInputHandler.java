package mafi.core.input;

import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import mafi.core.Window;
import mafi.core.view.View;

/**
 *
 * @author vito
 */
public class BasicViewInputHandler implements ViewInputHandler{

	protected Window wd = null;
	protected View view = null;

	protected Point mousePoint = new Point(0, 0);
	boolean isAnimate = false;

	public Window getWorldWindow(){
		return this.wd;
	}
    public void setWorldWindow(Window newWorldWindow){
		this.wd = newWorldWindow;
	}

	public void setView(View view)
	{
		this.view = view;
	}

	public View getView()
	{
		return this.view;
	}

	public void apply()
	{
	}

	public boolean isAnimate(){
		return this.isAnimate;
	}

	public Point getMousePoint(){
		return this.mousePoint;
	}

	public void focusGained(FocusEvent e)
	{
	}

	public void focusLost(FocusEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseDragged(MouseEvent e)
	{
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
	}
}
