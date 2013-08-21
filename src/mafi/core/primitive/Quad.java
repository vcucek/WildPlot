package mafi.core.primitive;

import javax.media.opengl.GL;
import mafi.common.math.Matrix;
import mafi.common.math.Vec4;

/**
 *
 * @author vito
 */
public class Quad {

	Vec4 lb;
	Vec4 rb;
	Vec4 lt;
	Vec4 rt;

	public static Quad screenQuad = new Quad(new Vec4(-1,-1),
											 new Vec4( 1,-1),
											 new Vec4(-1, 1),
											 new Vec4( 1, 1));

	public Quad(Vec4 lb, Vec4 rb, Vec4 lt, Vec4 rt){
		this.lb = lb;
		this.rb = rb;
		this.lt = lt;
		this.rt = rt;
	}

	public void drawQuad(GL gl)
	{
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		gl.glVertex3d(this.lb.x, this.lb.y, this.lb.z);
		gl.glVertex3d(this.rb.x, this.rb.y, this.rb.z);
		gl.glVertex3d(this.lt.x, this.lt.y, this.lt.z);
		gl.glVertex3d(this.rt.x, this.rt.y, this.rt.z);
		gl.glEnd();
	}

	public Quad transform(Matrix m){
		return (new Quad(lb.transformBy4(m),
						 rb.transformBy4(m),
						 lt.transformBy4(m),
						 rt.transformBy4(m)));
	}

}
