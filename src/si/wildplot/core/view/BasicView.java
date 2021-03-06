package si.wildplot.core.view;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Angle;
import si.wildplot.common.math.Matrix;
import si.wildplot.common.math.Vec4;
import si.wildplot.common.util.Logging;
import si.wildplot.core.DrawContext;
import si.wildplot.core.WildPlotObjectImpl;
import si.wildplot.core.input.BasicViewInputHandler;
import si.wildplot.core.input.ViewInputHandler;
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
public class BasicView extends WildPlotObjectImpl implements View{

	protected DrawContext dc;

	protected Vec4 position = Vec4.ZERO;
	protected Vec4 centerPosition = Vec4.ZERO;
	protected Vec4 up = Vec4.UNIT_Y;
	protected Vec4 forward = Vec4.UNIT_NEGATIVE_Z;

	protected Angle pitchAngle = Angle.ZERO;
	protected Angle headingAngle = Angle.ZERO;
	protected Angle rollAngle = Angle.ZERO;

	protected Sector visibleSector = Sector.EMPTY_SECTOR;

	private int currentMatrixMode = GL2.GL_MODELVIEW;

	protected Matrix model = Matrix.IDENTITY;
	protected Matrix view = Matrix.IDENTITY;
	protected Matrix modelView = Matrix.IDENTITY;
	protected Matrix projection = Matrix.IDENTITY;
	protected Matrix modelViewProjection = Matrix.IDENTITY;
	protected Matrix modelViewProjectionI = Matrix.IDENTITY;

	protected double fovWidth = 1.0d;
	protected double fovHeight = 1.0d;
	protected double fovNear = 0.1d;
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

	public void setMatrixMode(GL2 gl, int mode){

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
