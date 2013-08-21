package mafi.core.input;

import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.media.opengl.GLCanvas;
import mafi.common.math.Matrix;
import mafi.common.math.Vec4;

/**
 *
 * @author vito
 */
public class ViewInputHandler2D extends BasicViewInputHandler{

	private boolean hasFocus = false;
//	private Point mousePosition = null;
	private Point mousePositionStart = null;

	private Vec4 mousePositionWorld = Vec4.ZERO;
	private Vec4 mousePositionCamera = Vec4.ZERO;



	@Override
	public void apply()
	{
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		hasFocus = true;
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		hasFocus = false;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.wd.redraw();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.mousePositionStart = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		this.mousePoint = e.getPoint();
		calcMousePositionWorld();
		calcMousePositionCamera();
		
		double width = (double)((GLCanvas)this.wd).getWidth();
		double height = (double)((GLCanvas)this.wd).getHeight();

		double fovX = this.wd.getView().getFovWidtht();
		double fovY = this.wd.getView().getFovHeight();

		double moveX = - e.getPoint().x + this.mousePositionStart.x;
		double moveY = e.getPoint().y - this.mousePositionStart.y;
		Vec4 move = new Vec4(moveX, moveY);
		this.mousePositionStart = e.getPoint();

		Vec4 newCamPosition = this.wd.getView().getPosition()
				.add3(move.transformBy3(Matrix.fromScale(2.0d * fovX/width, 2.0d * fovY/height, 0.0d)));

//		System.out.println("MOUSE POS: " + e.getPoint().x + " , " + e.getPoint().y);

		this.wd.getView().setPosition(newCamPosition);
		this.wd.redraw();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		this.mousePoint = e.getPoint();
		calcMousePositionWorld();
		calcMousePositionCamera();
		this.wd.redraw();
	}

	private void calcMousePositionWorld(){
		calcMousePositionCamera();
		this.mousePositionWorld = this.mousePositionCamera.transformBy4(this.wd.getView().calcModelViewProjection().getInverse());
	}

	private void calcMousePositionCamera(){
		this.mousePositionCamera = transformPointToCam(this.mousePoint);
	}

	public Vec4 transformPointToCam(Point p){
		double width = (double)((GLCanvas)this.wd).getWidth();
		double height = (double)((GLCanvas)this.wd).getHeight();

		double xW = 2.0 * (double)p.x/width;
		double yW = 2.0 * (double)p.y/height;

		xW = xW - 1.0d;
		yW = 1.0d - yW;

		return (new Vec4(xW, yW));
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		double wheel = e.getWheelRotation();
		double fovX = this.wd.getView().getFovWidtht();
		double fovY = this.wd.getView().getFovHeight();

		double zoomFac = (1.0 + 0.1 * wheel);
		double fovWidthNew = fovX * zoomFac;
		double fovHeightNew = fovY * zoomFac;

		calcMousePositionCamera();
		Vec4 newCamPositionCam = this.mousePositionCamera.multiply3(1.0d - zoomFac)
				.transformBy4(this.wd.getView().calcModelViewProjection().getInverse());

		Vec4 currentCamPosition = this.wd.getView().getPosition();
		if(e.isControlDown())
		{
			this.wd.getView().setFovWidth(fovWidthNew);
			this.wd.getView().setPosition(new Vec4(newCamPositionCam.x, currentCamPosition.y));
		}
		else if(e.isAltDown()){
			this.wd.getView().setFovHeight(fovHeightNew);
			this.wd.getView().setPosition(new Vec4(currentCamPosition.x, newCamPositionCam.y));
		}
		else{
			this.wd.getView().setFovWidth(fovWidthNew);
			this.wd.getView().setFovHeight(fovHeightNew);
			this.wd.getView().setPosition(newCamPositionCam);
		}
		this.wd.redraw();
	}
}
