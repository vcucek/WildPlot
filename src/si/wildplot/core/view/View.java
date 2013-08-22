package si.wildplot.core.view;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Angle;
import si.wildplot.common.math.Matrix;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.DrawContext;
import si.wildplot.core.WildPlotObject;
import si.wildplot.core.input.ViewInputHandler;
import si.wildplot.core.primitive.Sector;

/**
 *
 * @author vito
 */
public interface View extends WildPlotObject{

	public void apply(DrawContext dc);

	public Vec4 getPosition();
	public void setPosition(Vec4 position);
	public Vec4 getDirection();
//	public Vec4 getCenterPosition();
//	public void setCenterPosition(Vec4 center);
	public Vec4 getUp();

	public Angle getPitch();
	public Angle getRoll();
	public Angle getHeading();
	public void setPitch(Angle pitch);
	public void setRoll(Angle roll);
	public void setHeading(Angle yaw);

	public Sector getVisibleSector();

	public void setFovHeight(double height);
	public double getFovHeight();
	public void setFovWidth(double width);
	public double getFovWidtht();
	public void setFovNear(double height);
	public double getFovNear();
	public void setFovFar(double height);
	public double getFovFar();

	public void setMatrixMode(GL2 gl, int mode);

	public Matrix getViewMatrix();
	public Matrix getModelMatrix();
	public Matrix getModelViewMatrix();
	public Matrix getProjectionMatrix();
	public Matrix getModelViewProjection();
	public Matrix getModelViewProjectionI();

	public Matrix calcModelMatrix();
	public Matrix calcViewMatrix();
	public Matrix calcModelViewMatrix();
	public Matrix calcProjectionMatrix();
	public Matrix calcModelViewProjection();

	public double getPixelWidth(double distance);
	public double getPixelHeight(double distance);

	public ViewInputHandler getInputHandler();
}
