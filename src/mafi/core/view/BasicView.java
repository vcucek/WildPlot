package mafi.core.view;

import javax.media.opengl.GL;
import mafi.common.math.Angle;
import mafi.common.math.Matrix;
import mafi.common.math.Vec4;
import mafi.common.util.Logging;
import mafi.core.DrawContext;
import mafi.core.MafiObjectImpl;
import mafi.core.input.BasicViewInputHandler;
import mafi.core.input.ViewInputHandler;
import mafi.core.primitive.Sector;

/**
 *
 * @author vito
 */
public class BasicView extends MafiObjectImpl implements View{

	protected DrawContext dc;

	protected Vec4 position = Vec4.ZERO;
	protected Vec4 centerPosition = Vec4.ZERO;
	protected Vec4 up = Vec4.UNIT_Y;
	protected Vec4 forward = Vec4.UNIT_NEGATIVE_Z;

	protected Angle pitchAngle = Angle.ZERO;
	protected Angle headingAngle = Angle.ZERO;
	protected Angle rollAngle = Angle.ZERO;

	protected Sector visibleSector = Sector.EMPTY_SECTOR;

	private int currentMatrixMode = GL.GL_MODELVIEW;

	protected Matrix model = Matrix.IDENTITY;
	protected Matrix view = Matrix.IDENTITY;
	protected Matrix modelView = Matrix.IDENTITY;
	protected Matrix projection = Matrix.IDENTITY;
	protected Matrix modelViewProjection = Matrix.IDENTITY;
	protected Matrix modelViewProjectionI = Matrix.IDENTITY;

	protected double fovWidth = 1.0d;
	protected double fovHeight = 1.0d;
	protected double fovNear = 1.0d;
	protected double fovFar = 10.0d;

	protected ViewInputHandler inputHandler = new BasicViewInputHandler();

	public void apply(DrawContext dc)
	{
		if (dc == null)
        {
            String message = Logging.getMessage("nullValue.DrawContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (dc.getGL() == null)
        {
            String message = Logging.getMessage("nullValue.DrawingContextGLIsNull");
            Logging.logger().severe(message);
            throw new IllegalStateException(message);
        }

		this.dc = dc;

		if(this.inputHandler != null){
			this.inputHandler.apply();
		}

		doApply(dc);
	}

	protected void doApply(DrawContext dc)
	{
	}

	public ViewInputHandler getInputHandler(){
		return this.inputHandler;
	}

	public double getPixelWidth(double distance){
		Vec4 temp = new Vec4(2.0d/(double)this.dc.getDrawableWidth(), 0.0d, distance);
		return (temp.transformBy3(this.modelViewProjectionI)).x;
	}

	public double getPixelHeight(double distance){
		Vec4 temp = new Vec4(0.0d, 2.0d/(double)this.dc.getDrawableHeight(), distance);
		return (temp.transformBy3(this.modelViewProjectionI)).y;
	}

	public Matrix calcModelMatrix(){
		return Matrix.IDENTITY;
	}
	public Matrix calcViewMatrix(){
		return Matrix.IDENTITY;
	}

	public Matrix calcModelViewMatrix()
	{
		return (calcViewMatrix().multiply(calcModelMatrix()));
	}

	public Matrix calcProjectionMatrix()
	{
		return Matrix.IDENTITY;
	}

	public Matrix calcModelViewProjection()
	{
		return (calcProjectionMatrix().multiply(calcModelViewMatrix()));
//		return (calcModelViewMatrix().multiply(calcProjectionMatrix()));
	}

	public void setMatrixMode(GL gl, int mode){

		if(this.currentMatrixMode == mode)
			return;
		gl.glMatrixMode(mode);
		this.currentMatrixMode = mode;
	}

	public Vec4 getPosition()
	{
		return position;
	}

	public void setPosition(Vec4 position)
	{
		this.position = position;
	}

	public Vec4 getDirection()
	{
		Matrix rotation = Matrix.fromRotationXYZ(this.getPitch(), this.getRoll(), this.getHeading());
		this.forward = Vec4.UNIT_NEGATIVE_Z.transformBy3(rotation);
		return this.forward;
	}

	public Vec4 getUp()
	{
		Matrix rotation = Matrix.fromRotationXYZ(this.getPitch(), this.getRoll(), this.getHeading());
		this.up = Vec4.UNIT_Y.transformBy3(rotation);
		return this.up;
	}

	public Angle getPitch(){
		return this.pitchAngle;
	}

	public Angle getRoll(){
		return this.rollAngle;
	}

	public Angle getHeading(){
		return this.headingAngle;
	}

	public void setPitch(Angle pitch){
		this.pitchAngle = pitch;
	}

	public void setRoll(Angle roll){
		this.rollAngle = roll;
	}

	public void setHeading(Angle yaw){
		this.headingAngle = yaw;
	}

	public Sector getVisibleSector(){
		return this.visibleSector;
	}

	public void setFovHeight(double height)
	{
		this.fovHeight = height;
	}

	public double getFovHeight()
	{
		return this.fovHeight;
	}

	public void setFovWidth(double width)
	{
		this.fovWidth = width;
	}

	public double getFovWidtht()
	{
		return this.fovWidth;
	}

	public void setFovNear(double near)
	{
		this.fovNear = near;
	}

	public double getFovNear()
	{
		return this.fovNear;
	}

	public void setFovFar(double far)
	{
		this.fovFar = far;
	}

	public double getFovFar()
	{
		return this.fovFar;
	}

	public Matrix getViewMatrix()
	{
		return this.view;
	}

	public Matrix getModelMatrix()
	{
		return model;
	}

	public Matrix getModelViewMatrix()
	{
		return this.modelView;
	}

	public Matrix getProjectionMatrix()
	{
		return this.projection;
	}

	public Matrix getModelViewProjection(){
		return this.modelViewProjection;
	}
	public Matrix getModelViewProjectionI(){
		return this.modelViewProjectionI;
	}
}
