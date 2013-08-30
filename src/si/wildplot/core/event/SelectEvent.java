package si.wildplot.core.event;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.EventObject;
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
public class SelectEvent extends EventObject{
	private static final long serialVersionUID = 1L;

	public static final String MANUAL = "mafi.core.event.SelectEvent.manual";
	public static final String LEFT_CLICK = "mafi.core.event.SelectEvent.LeftClick";
    public static final String LEFT_DOUBLE_CLICK = "mafi.core.event.SelectEvent.LeftDoubleClick";
    public static final String RIGHT_CLICK =  "mafi.core.event.SelectEvent.RightClick";
    public static final String LEFT_PRESS = "mafi.core.event.SelectEvent.LeftPress";
    public static final String RIGHT_PRESS = "mafi.core.event.SelectEvent.RightPress";
    public static final String HOVER =  "mafi.core.event.SelectEvent.Hover";
    public static final String ROLLOVER =  "mafi.core.event.SelectEvent.Rollover";
    public static final String DRAG =  "mafi.core.event.SelectEvent.Drag";
    public static final String DRAG_END =  "mafi.core.event.SelectEvent.DragEnd";

	private final String eventAction;
    private final MouseEvent mouseEvent;
    private final Point2D pickPoint;
	private final Renderable pickedRenderable;

	public SelectEvent(Object source, String eventAction, MouseEvent mouseEvent, Renderable pickedRenderable){
		super(source);
		this.eventAction = eventAction;
		this.mouseEvent = mouseEvent;
		this.pickPoint = mouseEvent.getPoint();
		this.pickedRenderable = pickedRenderable;
	}

	public SelectEvent(Object source, String eventAction, Point2D pickPoint, Renderable pickedRenderable){
		super(source);
		this.eventAction = eventAction;
		this.mouseEvent = null;
		this.pickPoint = pickPoint;
		this.pickedRenderable = pickedRenderable;
	}

	public SelectEvent(Object source, Renderable pickedRenderable){
		super(source);
		this.eventAction = MANUAL;
		this.mouseEvent = null;
		this.pickPoint = null;
		this.pickedRenderable = pickedRenderable;
	}

	public String getEventAction(){
		return this.eventAction != null ? this.eventAction : "mafi.core.event.UnknownEventAction";
	}

	public Renderable getPickedRenderable(){
		return this.pickedRenderable;
	}

	public MouseEvent getMouseEvent(){
		return this.mouseEvent;
	}

}
