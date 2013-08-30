package si.wildplot.core.view;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Matrix;
import si.wildplot.core.DrawContext;
import si.wildplot.core.input.ViewInputHandler2D;
import si.wildplot.core.primitive.Sector;

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
public class View2D extends BasicView{

	public View2D()
	{
		this.inputHandler = new ViewInputHandler2D();
		this.inputHandler.setView(this);
	}

	@Override
	protected void doApply(DrawContext dc){
		this.projection = calcProjectionMatrix();
		this.view = calcViewMatrix();
		this.modelView = calcModelViewMatrix();

		this.modelViewProjection = calcModelViewProjection();
		this.modelViewProjectionI = this.modelViewProjection.getInverse();

		Sector unitSector = new Sector(-1, -1, 1, 1);
		this.visibleSector = unitSector.transform(modelViewProjection.getInverse());
//		System.out.print(this.visibleSector.getCenter().x +" , "+ this.visibleSector.getCenter().y);
//		System.out.println("   " + this.visibleSector.getWidth() +" , "+ this.visibleSector.getHeight());

		GL2 gl = dc.getGL().getGL2();
		double[] matrixArray = new double[16];
		setMatrixMode(gl, GL2.GL_PROJECTION);
		gl.glLoadMatrixd(this.projection.toArray(matrixArray, 0, false), 0);
		setMatrixMode(gl, GL2.GL_MODELVIEW);
		gl.glLoadMatrixd(this.modelView.toArray(matrixArray, 0, false), 0);

		gl.glDisable(GL.GL_DEPTH_TEST);
	}

	@Override
	public Matrix calcViewMatrix()
	{
		return Matrix.fromTranslation(-position.x, -position.y, -1.0d);
	}

	@Override
	public Matrix calcProjectionMatrix()
	{
		return Matrix.fromOrthographic(2.0d * fovWidth, 2.0d * fovHeight, 1.0d, 10.0d);
	}
}
