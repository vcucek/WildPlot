package si.wildplot.core.layer;

import java.util.ArrayList;
import si.wildplot.common.util.GLTaskService;
import si.wildplot.common.util.Logging;
import si.wildplot.core.DrawContext;
import si.wildplot.core.WildPlotObjectImpl;
import si.wildplot.core.event.SelectEvent;
import si.wildplot.core.render.Renderable;

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
public class BasicLayer extends WildPlotObjectImpl implements Layer{

	private final String name;
	private int position;
	private ArrayList<Renderable> renderables = new ArrayList<Renderable>();

	private boolean isPickEnabled = false;

	public BasicLayer(String name, int position)
	{
		this.name = name;
		this.position = position;
	}

	public String getName()
	{
		return name;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public int getPosition()
	{
		return this.position;
	}

	public synchronized void addRenderable(Renderable r)
	{
		if(r == null){
			String msg = Logging.getMessage("WARNING in method addRenderable() "
					+ ":: cant add null Renderable in Layer!");
            Logging.logger().warning(msg);
			return;
		}
		this.addPropertyChangeListener(r);
		renderables.add(r);
	}

	public synchronized void removeRenderable(Renderable r)
	{
		if(r == null){
			String msg = Logging.getMessage("WARNING in method removeRenderable() "
					+ ":: cant remove null Renderable in Layer!");
            Logging.logger().warning(msg);
			return;
		}

		this.removePropertyChangeListener(r);
		GLTaskService.getInstance().addTaskDispose(r);
		renderables.remove(r);
	}

	public synchronized ArrayList<Renderable> getRenderables()
	{
		return ((ArrayList<Renderable>)renderables.clone());
	}

	public void preRender(DrawContext dc)
	{
		for(Renderable r : this.getRenderables()){
			r.preRender(dc);
		}
	}

	public void pick(DrawContext dc)
	{
		if(!dc.isPickingEnabled())
			return;

		if(!this.isPickEnabled())
			return;

		for(Renderable r : this.getRenderables()){
			if(r.isPickEnabled()){
				r.pick(dc);
			}
		}
	}

	public void render(DrawContext dc)
	{
		for(Renderable r : this.getRenderables()){
			r.render(dc);
		}
	}

	public synchronized void dispose(DrawContext dc)
	{
		for(Renderable r : this.getRenderables()){
			r.dispose(dc);
		}

		this.renderables.clear();
	}

	public void setPickEnabled(boolean enable)
	{
		this.isPickEnabled = enable;
	}

	public boolean isPickEnabled()
	{
		return this.isPickEnabled;
	}

	public void selected(SelectEvent event)
	{
		for(Renderable r : this.getRenderables()){
			r.selected(event);
		}
	}
}
