package si.wildplot.core.render;

import si.wildplot.core.DrawContext;
import si.wildplot.core.WildPlotObject;
import si.wildplot.core.event.SelectListener;

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
public interface Renderable extends WildPlotObject, SelectListener{

	public void preRender(DrawContext dc);
	public void pick(DrawContext dc);
	public void render(DrawContext dc);
	public void dispose(DrawContext dc);

	public boolean isPickEnabled();
}
