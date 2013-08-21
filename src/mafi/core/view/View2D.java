package mafi.core.view;

import javax.media.opengl.GL;
import mafi.common.math.Matrix;
import mafi.core.DrawContext;
import mafi.core.input.ViewInputHandler2D;
import mafi.core.primitive.Sector;

/**
 *
 * @author vito
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

		GL gl = dc.getGL();
		double[] matrixArray = new double[16];
		setMatrixMode(gl, GL.GL_PROJECTION);
		gl.glLoadMatrixd(this.projection.toArray(matrixArray, 0, false), 0);
		setMatrixMode(gl, GL.GL_MODELVIEW);
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
