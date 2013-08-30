package si.wildplot.core;

import si.wildplot.core.view.View;
import si.wildplot.core.render.Renderable;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.event.InputHandler;
import si.wildplot.core.event.SelectListener;
import si.wildplot.core.jocl.BasicCLContext;
import si.wildplot.core.render.Model;

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
public interface Window
{
	public AbstractSceneController getSceneController();
	public BasicCLContext getCLContext();

	public InputHandler getInputHandler();
	public void setInputHandler(InputHandler inputHandler);

	public void setView(View view);
	public View getView();

	public Model getModel();
	public void setModel(Model model);

	public void redraw();
	public void redrawNow();
	public void shutdown();

	public Vec4 getPosition();
	public Renderable getSelection();
	public void setSelection(Renderable renderable);

	public void addSelectListener(SelectListener listener);
	public void removeSelectListener(SelectListener listener);
}
