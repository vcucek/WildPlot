package si.wildplot.core.input;

import java.awt.Point;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import si.wildplot.core.Window;
import si.wildplot.core.view.View;

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
