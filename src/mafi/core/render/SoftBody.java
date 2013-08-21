package mafi.core.render;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import javax.media.opengl.GL;
import mafi.common.math.Vec4;
import mafi.core.DrawContext;
import mafi.core.event.SelectEvent;
import mafi.core.primitive.Box;

/**
 *
 * @author vito
 */

//2d soft body animation!!

public class SoftBody implements Renderable, MouseMotionListener{

	HashSet<MassPoint> mpSet = new HashSet<MassPoint>();
	private double springK = 1800.0;
	private double springKD = 2500.0;
	private double springB = 18.0;
	private Vec4 g = new Vec4(0.0, 9.8);
//	private Vec4 g = new Vec4(0.0, 0.0);

	private boolean init = true;
	private long startTimeMS;
	private long lastTimeMS;

	public void preRender(DrawContext dc)
	{
	}

	public void pick(DrawContext dc)
	{
	}

	private void init(){
		if(!init){
			return;
		}

		double xs = 0.0;
		double xe = 1.0;
		
		MassPoint mp1 = new MassPoint(new Vec4(xs, 0.0), 10000);
		mp1.enableAnimation(false);
		
		MassPoint mp2 = new MassPoint(new Vec4(xe, 0.0), 10000);
		mp2.enableAnimation(false);
		
		MassPoint mp3 = new MassPoint(new Vec4(xe, 0.5));
		mp3.setExternalForce(g.multiply3(mp3.getMass()));
		
		MassPoint mp4 = new MassPoint(new Vec4(xs, 0.5));
		mp4.setExternalForce(g.multiply3(mp4.getMass()));

//		mp3.attachObject(mp1, new Spring(springKD, springB));
//		mp3.attachObject(mp2, new Spring(springK, springB));
//		mp3.attachObject(mp4, new Spring(springK, springB));
		
//		mp4.attachObject(mp1, new Spring(springK, springB));
//		mp4.attachObject(mp2, new Spring(springKD, springB));
//		mp4.attachObject(mp3, new Spring(springK, springB));

		
		mp3.attachObject(mp1, new Spring(springK, springB));
		mp3.attachObject(mp2, new Spring(springK, springB));
		mp4.attachObject(mp1, new Spring(springK, springB));
		mp4.attachObject(mp2, new Spring(springK, springB));
		mp4.attachObject(mp3, new Spring(4000, 200));

		//extra points

		MassPoint last1 = mp4;
		MassPoint last2 = mp3;
		
		for(int i=0; i<1; i++){
			MassPoint mpe2 = new MassPoint(new Vec4(xe, 1.0 + 0.5*i));
			mpe2.setExternalForce(g.multiply3(mp3.getMass()));
		
			MassPoint mpe1 = new MassPoint(new Vec4(xs, 1.0 + 0.5*i));
			mpe1.setExternalForce(g.multiply3(mp4.getMass()));

			double dump = 120.0;
			double sK = 3000;
			double sKD = 3000;
			Spring sd = new Spring(sKD, dump);
			Spring s = new Spring(sK, dump);
			mpe1.attachObject(last2, sd);
			mpe1.attachObject(last1, s);
			mpe1.attachObject(mpe2, s);

			mpe2.attachObject(last1, sd);
			mpe2.attachObject(last2, s);
			mpe2.attachObject(mpe1, s);

			mpSet.add(mpe1);	
			mpSet.add(mpe2);

			last1 = mpe1;
			last2 = mpe2;
		}
		///////////////////////////////////

		mpSet.add(mp1);	
		mpSet.add(mp2);	
		mpSet.add(mp3);	
		mpSet.add(mp4);	

		Box constrainBox = new Box(-1, 2, 0.2, 2, -1, 1);
		for(MassPoint p : this.mpSet){
			p.setConstrains(constrainBox);
		}

		this.startTimeMS = System.currentTimeMillis();
		this.lastTimeMS = 0;
		this.init = false;
	}

	public void render(DrawContext dc)
	{
		GL gl = dc.getGL();
		init();

		long time = System.currentTimeMillis() - this.startTimeMS;
		double dTime = ((double)(time - this.lastTimeMS))/1000.0;
		for(MassPoint mp : mpSet){
//			mp.update(dTime/100.0);
//			mp.update(dTime);
//			mp.update(0.0002);
			mp.update(0.005);
		}

		gl.glPointSize(8);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_POINTS);
		for(MassPoint mp : mpSet){
			mp.render(gl);
		}
		gl.glEnd();
		gl.glPointSize(1);

		for(MassPoint mp : mpSet){
			mp.flush();
		}

		this.lastTimeMS = time;
		dc.requestRedraw();
	}

	public void dispose(DrawContext dc)
	{
	}

	public boolean isPickEnabled()
	{
		return false;
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
	}

	public void firePropertyChange(PropertyChangeEvent propertyChangeEvent)
	{
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
	}

	public void selected(SelectEvent event)
	{
	}

	private Vec4 startDrag;
	private boolean drag = false;
	public void mouseDragInit(MouseEvent e)
	{
		if(drag == true){
			return;
		}
		this.startDrag = new Vec4(e.getPoint().x, e.getPoint().y);
		this.drag = true;
	}

	public void mouseDragged(MouseEvent e)
	{
		if(!(e.isShiftDown())){
			return;
		}
		mouseDragInit(e);
		Vec4 mousePosition = new Vec4(e.getX(), e.getY());
		Vec4 dragVec = mousePosition.subtract3(startDrag);
		for(MassPoint p : this.mpSet){
			if(p.isAnimationEnabled()){
				Vec4 fg = g.multiply3(p.getMass()); 
				p.setExternalForce(fg.add3(dragVec));
			}
		}
		e.consume();
	}

	public void mouseMoved(MouseEvent e)
	{
		if(!drag){
			return;
		}
		for(MassPoint p : this.mpSet){
			if(p.isAnimationEnabled()){
				p.setExternalForce(g.multiply3(p.getMass()));
			}
		}
		this.drag = false;
	}
	
}
