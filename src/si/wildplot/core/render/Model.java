package si.wildplot.core.render;

import java.util.ArrayList;
import si.wildplot.core.WildPlotObject;
import si.wildplot.core.event.SelectEvent;
import si.wildplot.core.event.SelectListener;
import si.wildplot.core.layer.Layer;

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
public interface Model extends WildPlotObject, SelectListener{

	public void addLayer(String name, int position);
	public void addLayer(Layer layer);
	public void setCurrentLayer(String name);
	public void setCurrentLayer(Layer layer);
	public void pushCurrentLayer();
	public void popCurrentLayer();
	public void removeLayer(String name);
	public void removeLayer(Layer layer);

	public Layer getLayer(String name);

	public void addRenderable(Renderable r);
	public void removeRenderable(Renderable r);
	public ArrayList<Renderable> getRenderables();

	public void setCurrentSelection(Renderable r);
	public void setCurrentSelection(SelectEvent event);
	public Renderable getCurrentSelection();

	public Iterable<Layer> getLayers();

	public void dispose();
}
