package mafi.core.view;

import javax.media.opengl.GL;
import mafi.common.math.Angle;
import mafi.common.math.Matrix;
import mafi.common.math.Vec4;
import mafi.core.DrawContext;
import mafi.core.MafiObject;
import mafi.core.input.ViewInputHandler;
import mafi.core.primitive.Sector;

/**
 *
 * @author vito
 */
public interface View extends MafiObject{

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

	public void setMatrixMode(GL gl, int mode);

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
