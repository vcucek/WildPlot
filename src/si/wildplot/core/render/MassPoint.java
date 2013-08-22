package si.wildplot.core.render;

import java.util.HashMap;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.primitive.Box;

/**
 *
 * @author vito
 */
class MassPoint {
	
	private HashMap<MassPoint, Spring> conectedMassPoints = new HashMap<MassPoint, Spring>();
	private boolean enableAnim = true;
	private Box boxConstrain = null;
	
	//array 0 is current data and 1 is last;
	private double mass = 10;
	private Vec4[] position = new Vec4[2];
	private Vec4[] velocity = new Vec4[2];
	private Vec4[] acceleration = new Vec4[2];
	private Vec4 externalForce = Vec4.ZERO;

	public MassPoint(Vec4 initPosition)
	{
		position[1] = initPosition;
		position[0] = initPosition;
		velocity[1] = Vec4.ZERO;
		velocity[0] = Vec4.ZERO;
		acceleration[1] = Vec4.ZERO;
		acceleration[0] = Vec4.ZERO;
	}

	public MassPoint(Vec4 initPosition, double mass)
	{
		this(initPosition);
		this.mass = mass;
	}

	public double getMass(){
		return mass;
	}

	public void attachObject(MassPoint mp, Spring spring){
		Spring s = spring;
		if(spring.springLength <= 0.0){
			double len = position[1].subtract3(mp.position[1]).getLength3();	
			s = new Spring(spring.springK, spring.springB, len);
		}
		conectedMassPoints.put(mp, s);
		mp.conectedMassPoints.put(this, s);
	}

	public void setExternalForce(Vec4 force)
	{
		this.externalForce = force;
	}

	public void enableAnimation(boolean enable)
	{
		this.enableAnim = enable;
	}

	public boolean isAnimationEnabled(){
		return this.enableAnim;
	}

	public void update(double dt)
	{
		if (!this.enableAnim) {
			return;
		}
		//calc force
		Vec4 forceSum = Vec4.ZERO;
		Vec4 dragForceSum = Vec4.ZERO;
		
		for (MassPoint mp : conectedMassPoints.keySet()) {
			Spring s = conectedMassPoints.get(mp); 
			Vec4 diffV = position[1].subtract3(mp.position[1]);
			Vec4 dirV = diffV.normalize3();
			double length = diffV.getLength3();
			forceSum = forceSum.add3(dirV.multiply3((s.springLength - length) * s.springK));

			//dumping
			double dp = diffV.getLength3();
			Vec4 addV = (velocity[1].subtract3(mp.velocity[1])).multiply3(dt);
			double ddp = (diffV.add3(addV)).getLength3();
			double v = (ddp-dp)/dt;
			dragForceSum = dragForceSum.add3(dirV.multiply3(-s.springB * v/0.5));
		}
		forceSum = forceSum.add3(this.externalForce);

		acceleration[0] = (forceSum.add3(dragForceSum)).divide3(mass);
		velocity[0] = velocity[1].add3(acceleration[0].multiply3(dt));
		
		position[0] = position[1].add3(velocity[1].multiply3(dt));
		position[0] = position[0].add3(acceleration[1].multiply3(dt*dt/2.0));
		
		if(this.boxConstrain != null){
			position[0] = this.boxConstrain.clamp(position[0]);
		}
	}

	public void flush()
	{
		position[1] = position[0];
		velocity[1] = velocity[0];
		acceleration[1] = acceleration[0];
	}

	public void setConstrains(Box box){
		this.boxConstrain = box;	
	}

	public void render(GL2 gl)
	{
		gl.glVertex2d(position[0].x, position[0].y);
	}
	
}
