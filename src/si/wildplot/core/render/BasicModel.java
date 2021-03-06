package si.wildplot.core.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import si.wildplot.common.util.GLTaskService;
import si.wildplot.common.util.Logging;
import si.wildplot.core.WildPlotObjectImpl;
import si.wildplot.core.event.SelectEvent;
import si.wildplot.core.layer.BasicLayer;
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
public class BasicModel extends WildPlotObjectImpl implements Model{

	private TreeMap<Integer, Layer> layers = new TreeMap<Integer, Layer>();
	
	private Layer currentLayer = null;
	private Layer lastLayer = null;

	private Renderable currentSelection = null;

	private final Thread threadLock;

	public BasicModel()
	{
		this.threadLock = Thread.currentThread();
	}

	public synchronized void addLayer(Layer layer)
	{
		this.addPropertyChangeListener(layer);
		layers.put(layer.getPosition(), layer);
	}

	public synchronized void addLayer(String name, int position)
	{
		Layer newLayer = new BasicLayer(name, position);
		this.addPropertyChangeListener(newLayer);
		layers.put(newLayer.getPosition(), newLayer);
	}

	public Layer getLayer(String name){
		Layer outLayer = null;
		for(Layer layer : layers.values()){
			if(layer.getName().equals(name)){
				outLayer = layer;
			}
		}
		if(outLayer == null){
			String msg = Logging.getMessage("ERROR in method getLayer() :: Layer: "
					+ name + " can not be found in current Model");
            Logging.logger().warning(msg);
		}
		return outLayer;
	}

	public void setCurrentLayer(String name)
	{
		if(!this.threadLock.equals(Thread.currentThread())){
			String msg = Logging.getMessage("ERROR in method setCurrentLayer() ::"
					+ " This method can not be called from another thread!");
            Logging.logger().severe(msg);
			throw new IllegalStateException(msg);
		}

		Layer layer = this.getLayer(name);
		this.currentLayer = layer;

		if(layer == null){
			String msg = Logging.getMessage("WARNING in method setCurrentLayer() :: currentLayer is set to null!");
            Logging.logger().warning(msg);
		}
	}

	public void setCurrentLayer(Layer layer)
	{
		if(!this.threadLock.equals(Thread.currentThread())){
			String msg = Logging.getMessage("ERROR in method setCurrentLayer() ::"
					+ " This method can not be called from another thread!");
            Logging.logger().severe(msg);
			throw new IllegalStateException(msg);
		}

		if(layer == null){
			String msg = Logging.getMessage("WARNING in method setCurrentLayer() :: currentLayer is set to null!");
            Logging.logger().warning(msg);
			this.currentLayer = null;
			return;
		}

		if(layers.containsValue(layer)){
			this.currentLayer = layer;
		}
		else{
			String msg = Logging.getMessage("ERROR in method setCurrentLayer() :: Layer: "
					+ layer.getName() + " can not be found in current Model");
            Logging.logger().severe(msg);
		}
	}

	public void pushCurrentLayer()
	{
		if(!this.threadLock.equals(Thread.currentThread())){
			String msg = Logging.getMessage("ERROR in method pushCurrentLayer() ::"
					+ " This method can not be called from another thread!");
            Logging.logger().severe(msg);
			throw new IllegalStateException(msg);
		}

		this.lastLayer = this.currentLayer;
	}

	public void popCurrentLayer()
	{
		if(!this.threadLock.equals(Thread.currentThread())){
			String msg = Logging.getMessage("ERROR in method popCurrentLayer() ::"
					+ " This method can not be called from another thread!");
            Logging.logger().severe(msg);
			throw new IllegalStateException(msg);
		}

		if(this.lastLayer == null){
			String msg = Logging.getMessage("ERROR in method popCurrentLayer() :: pop before push!");
            Logging.logger().severe(msg);
		}
		else{
			this.currentLayer = this.lastLayer;
		}
	}

	public synchronized void removeLayer(String name)
	{
		Layer layer = this.getLayer(name);

		if(layer == null){
			String msg = Logging.getMessage("WARNING in method removeLayer() :: cant remove null layer!");
            Logging.logger().warning(msg);
			return;
		}

		GLTaskService.getInstance().addTaskDispose(layer);
		layers.remove(layer.getPosition());
	}

	public synchronized void removeLayer(Layer layer)
	{
		if(layer == null){
			String msg = Logging.getMessage("WARNING in method removeLayer() :: cant remove null layer!");
            Logging.logger().warning(msg);
			return;
		}

		GLTaskService.getInstance().addTaskDispose(layer);
		layers.remove(layer.getPosition());
	}

	public synchronized void addRenderable(Renderable r)
	{
		if(this.currentLayer == null)
			return;

		this.currentLayer.addRenderable(r);
	}

	public synchronized void removeRenderable(Renderable r)
	{
		if(this.currentLayer == null)
			return;

		this.currentLayer.removeRenderable(r);
	}

	public ArrayList<Renderable> getRenderables()
	{
		if(this.currentLayer == null)
			return new ArrayList<Renderable>();

		return this.currentLayer.getRenderables();
	}

	public Iterable<Layer> getLayers()
	{
		return ((TreeMap)layers.clone()).values();
	}

	public void selected(SelectEvent event)
	{
		for(Layer layer : getLayers()){
			layer.selected(event);
		}
	}

	public void setCurrentSelection(Renderable r)
	{
		this.currentSelection = r;
		this.selected(new SelectEvent(this, r));
	}

	public void setCurrentSelection(SelectEvent event)
	{
		this.currentSelection = event.getPickedRenderable();
		selected(event);
	}

	public Renderable getCurrentSelection()
	{
		return this.currentSelection;
	}

	public synchronized  void dispose()
	{
		for(Layer layer : this.layers.values()){
			GLTaskService.getInstance().addTaskDispose(layer);
		}
		this.layers.clear();
		this.lastLayer = null;
		this.currentLayer = null;
		this.currentSelection = null;
	}
}
