package si.wildplot.core.view;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Angle;
import si.wildplot.common.math.Matrix;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.DrawContext;
import si.wildplot.core.input.ViewInputHandlerCombined3D2D;
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
public class ViewCombined3D2D extends BasicView{

	private View2D view2d = new View2D();

	public ViewCombined3D2D()
	{
		this.position = Vec4.UNIT_Z.multiply3(1.5d);
		this.inputHandler = new ViewInputHandlerCombined3D2D();
		this.inputHandler.setView(this);
	}

	public View2D getView2d(){
		return view2d;
	}

	@Override
	public Sector getVisibleSector()
	{
		return view2d.getVisibleSector();
	}


	@Override
	protected void doApply(DrawContext dc){

		view2d.apply(dc);

		this.projection = calcProjectionMatrix();
		this.view = calcViewMatrix();
		this.modelView = calcModelViewMatrix();

		this.modelViewProjection = calcModelViewProjection();
		this.modelViewProjectionI = this.modelViewProjection.getInverse();

		this.visibleSector = view2d.getVisibleSector();
//		System.out.print(this.visibleSector.getCenter().x +" , "+ this.visibleSector.getCenter().y);
//		System.out.println("   " + this.visibleSector.getWidth() +" , "+ this.visibleSector.getHeight());

		GL2 gl = dc.getGL().getGL2();
		double[] matrixArray = new double[16];
		setMatrixMode(gl, GL2.GL_PROJECTION);
		gl.glViewport(0, 0, this.dc.getDrawableWidth(), this.dc.getDrawableHeight());
		gl.glLoadMatrixd(this.projection.toArray(matrixArray, 0, false), 0);
		setMatrixMode(gl, GL2.GL_MODELVIEW);
		gl.glLoadMatrixd(this.modelView.toArray(matrixArray, 0, false), 0);

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
	}

	@Override
	public Matrix calcViewMatrix()
	{
		Matrix translate = Matrix.fromTranslation(this.position);
		Matrix rotate = Matrix.fromRotationXYZ(this.getPitch(), this.getRoll(), this.getHeading());
		return rotate.multiply(translate.getInverse());
	}

	@Override
	public Matrix calcProjectionMatrix()
	{
//		return Matrix.fromOrthographic(0.005 * fovWidth * this.dc.getDrawableWidth(), 0.005 * fovHeight * this.dc.getDrawableHeight(), 1.0d, 10.0d);
		return Matrix.fromPerspective(0.0005 * fovWidth * this.dc.getDrawableWidth(), 0.0005 * fovHeight * this.dc.getDrawableHeight(), fovNear, fovFar);
	}

}
