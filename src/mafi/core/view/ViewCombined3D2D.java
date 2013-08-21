package mafi.core.view;

import javax.media.opengl.GL;
import mafi.common.math.Angle;
import mafi.common.math.Matrix;
import mafi.common.math.Vec4;
import mafi.core.DrawContext;
import mafi.core.input.ViewInputHandlerCombined3D2D;
import mafi.core.primitive.Sector;

/**
 *
 * @author vito
 */
public class ViewCombined3D2D extends BasicView{

	private View2D view2d = new View2D();

	public ViewCombined3D2D()
	{
		this.position = Vec4.UNIT_Z.multiply3(5.0d);
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

		GL gl = dc.getGL();
		double[] matrixArray = new double[16];
		setMatrixMode(gl, GL.GL_PROJECTION);
		gl.glViewport(0, 0, this.dc.getDrawableWidth(), this.dc.getDrawableHeight());
		gl.glLoadMatrixd(this.projection.toArray(matrixArray, 0, false), 0);
		setMatrixMode(gl, GL.GL_MODELVIEW);
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
		return Matrix.fromPerspective(0.005 * fovWidth * this.dc.getDrawableWidth(), 0.005 * fovHeight * this.dc.getDrawableHeight(), fovNear, fovFar);
	}

}
