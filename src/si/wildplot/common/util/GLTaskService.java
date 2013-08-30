package si.wildplot.common.util;

import java.util.HashSet;
import java.util.Iterator;
import si.wildplot.core.DrawContext;
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

public class GLTaskService {

	private int maxTaskPerFrame = 1;
	private int maxTaskQueueSize = 10000;

	private HashSet<Renderable> taskQueue = new HashSet<Renderable>();
	private HashSet<Renderable> taskQueueDispose = new HashSet<Renderable>();

	private static GLTaskService instance = null;

	public static synchronized GLTaskService getInstance(){
		if(instance == null){
			instance = new GLTaskService();
		}
		return instance;
	}

	public synchronized boolean addTaskDispose(Renderable taskDispose){

		if(taskDispose == null)
			return false;

		if(!taskQueueDispose.contains(taskDispose)){
			taskQueueDispose.add(taskDispose);
		}
		return true;
	}

	public synchronized boolean addTask(Renderable task){

		if(task == null)
			return false;

		if(taskQueue.size() >= maxTaskQueueSize){
			return false;
		}

		if(!taskQueue.contains(task)){
			taskQueue.add(task);
		}
		return true;
	}
	public synchronized void removeTask(Renderable task){
		if(taskQueue.contains(task)){
			taskQueue.remove(task);
		}
	}

	public boolean hasTasks(){
		return (!taskQueue.isEmpty() && !taskQueueDispose.isEmpty());
	}

	public boolean hasDisposeTasks(){
		return !taskQueueDispose.isEmpty();
	}

	public boolean isFull(){
		return (taskQueue.size() >= maxTaskQueueSize);
	}

	public synchronized void flush(DrawContext dc)
	{
		if(taskQueue.isEmpty() && taskQueueDispose.isEmpty()){
			return;
		}

		if(!taskQueue.isEmpty()){
			Iterator<Renderable> iter = taskQueue.iterator();

			int i = 0;
			while(iter.hasNext() && i<maxTaskPerFrame)
			{
				Renderable r = iter.next();
				r.preRender(dc);
				r.render(dc);
				iter.remove();
				i = i+1;
			}
		}

		if(!taskQueueDispose.isEmpty()){
			Iterator<Renderable> iter = taskQueueDispose.iterator();

			while(iter.hasNext())
			{
				iter.next().dispose(dc);
				iter.remove();
			}
			taskQueueDispose.clear();
		}
	}

	public synchronized void setMaxTaskPerFrame(int num){
		this.maxTaskPerFrame = num;
	}
}
