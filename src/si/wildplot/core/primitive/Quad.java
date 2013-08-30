package si.wildplot.core.primitive;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Matrix;
import si.wildplot.common.math.Vec4;

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

	public void drawQuad(GL2 gl)
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
