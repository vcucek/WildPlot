package si.wildplot.core.input;

import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.media.opengl.awt.GLCanvas;
import si.wildplot.common.math.Angle;
import si.wildplot.common.math.Matrix;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.primitive.Sector;
import si.wildplot.core.view.View;
import si.wildplot.core.view.View2D;
import si.wildplot.core.view.ViewCombined3D2D;

/**
 *
 * @author vito
 */
public class ViewInputHandlerCombined3D2D extends BasicViewInputHandler{

	private boolean hasFocus = false;
	private Point mousePositionStart = null;

	private boolean mouseRButtonDown = false;
	private boolean mouseLButtonDown = false;

	private View2D view2d;
	private long timeAnimator = 0;
	private long animateDelay = 600;

	@Override
	public void setView(View view)
	{
		super.setView(view);
		if(view instanceof ViewCombined3D2D){
			view2d = ((ViewCombined3D2D)this.view).getView2d();
		}
	}


	@Override
	public void apply()
	{
		if(this.isAnimate){
			if(animateDelay < System.currentTimeMillis() - timeAnimator){
				this.isAnimate = false;
			}
			//TODO:fix crash
			//this.wd.redraw();
		}
	}

	private void setAnimate(){
		this.timeAnimator = System.currentTimeMillis();
		this.isAnimate = true;
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
		Vec4 world = calcMousePositionWorld(e.getPoint());
		Vec4 camPos = this.wd.getView().getPosition();
		System.out.println("-----------------------------------------------------");
		System.out.println("MOUSEW: " + world.x + " " + world.y + " " + world.z);
		System.out.println("CAMPOS: " + camPos.x + " " + camPos.y + " " + camPos.z);
		System.out.println("-----------------------------------------------------");
		this.wd.redraw();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		setAnimate();
		this.mousePositionStart = e.getPoint();
		if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
      		mouseRButtonDown = true;
    	}
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
      		mouseLButtonDown = true;
    	}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		setAnimate();
		if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
			mouseRButtonDown = false;
		}
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			mouseLButtonDown = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(e.isConsumed())
			return;

		setAnimate();
		this.mousePoint = e.getPoint();

		double moveX = - e.getPoint().x + this.mousePositionStart.x;
		double moveY = e.getPoint().y - this.mousePositionStart.y;

		Vec4 newCamPosition3D = this.view.getPosition();
		Vec4 newCamPosition2D = this.view2d.getPosition();

		if(this.mouseLButtonDown){
			Vec4 moveModel = calcMousePositionWorld(mousePoint).subtract3(calcMousePositionWorld(this.mousePositionStart));
			Vec4 moveModelP = new Vec4(-moveModel.x, -moveModel.y);

			Sector sector = view2d.getVisibleSector();

			double moveLength = moveModel.getLength3();
			double width = 2.0 * (sector.maxX - sector.minX) * moveLength;
			double height = 2.0 * (sector.maxY - sector.minY) * moveLength;

//			Vec4 moveWorld = (moveModelP.normalize3().multiply3(moveModel.getLength3()));
			Vec4 moveWorld = (moveModelP.normalize3().multiply3(new Vec4(width, height)));
			newCamPosition2D = this.view2d.getPosition().add3(moveWorld);
		}
		if(this.mouseRButtonDown){
			Angle newHeading = this.view.getHeading().add(Angle.fromDegrees(-moveX*0.3));
			Angle newPitch = this.view.getPitch().add(Angle.fromDegrees(-moveY*0.3));
			newPitch = Angle.fromDegrees(Math.min(0.0d, newPitch.degrees));

			this.view.setHeading(newHeading);
			this.view.setPitch(newPitch);

			Matrix rotatePos = Matrix.fromRotationXYZ(newPitch, Angle.ZERO, newHeading);
			double camDistanceFromCenter = this.view.getPosition().getLength3();

			Vec4 newCamPositionModel = (Vec4.UNIT_Z.multiply3(camDistanceFromCenter)).transformBy3(rotatePos.getInverse());
			newCamPosition3D = newCamPositionModel;
		}
		this.view.setPosition(newCamPosition3D);
		this.view2d.setPosition(newCamPosition2D);

		this.wd.redraw();
		this.mousePositionStart = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		this.mousePoint = e.getPoint();
		this.wd.redraw();
	}

	private Vec4 calcMousePositionWorld(Point p){
		Vec4 mousePositionCamera = calcMousePositionCamera(p);
		return mousePositionCamera.transformBy4(this.wd.getView().calcModelViewProjection().getInverse());
	}

	private Vec4 calcMousePositionCamera(Point p){
		return transformPointToCam4(p);
	}

	public Vec4 transformPointToCam4(Point p){
		double width = (double)((GLCanvas)this.wd).getWidth();
		double height = (double)((GLCanvas)this.wd).getHeight();

		double xW = 2.0 * (double)p.x/width;
		double yW = 2.0 * (double)p.y/height;

		xW = xW - 1.0d;
		yW = 1.0d - yW;

		return (new Vec4(xW, yW, -1.0));
	}

	public Vec4 transformPointToCam3(Point p){
		double width = (double)((GLCanvas)this.wd).getWidth();
		double height = (double)((GLCanvas)this.wd).getHeight();

		double xW = 2.0 * ((double)p.x)/width;
		double yW = -2.0 * ((double)p.y)/height;

		return (new Vec4(xW, yW));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		setAnimate();
		double wheel = e.getWheelRotation();
		double fovX = this.view2d.getFovWidtht();
		double fovY = this.view2d.getFovHeight();

		double zoomFac = (1.0 + 0.1 * wheel);
		double fovWidthNew = fovX * zoomFac;
		double fovHeightNew = fovY * zoomFac;

		Vec4 newCamPositionCam = calcMousePositionCamera(mousePoint).multiply3(1.0d - zoomFac)
				.transformBy4(this.view2d.calcModelViewProjection().getInverse());

		Vec4 currentCamPosition = this.view2d.getPosition();
		if(e.isControlDown())
		{
			this.view2d.setFovWidth(fovWidthNew);
			this.view2d.setPosition(new Vec4(newCamPositionCam.x, currentCamPosition.y));
		}
		else if(e.isAltDown()){
			this.view2d.setFovHeight(fovHeightNew);
			this.view2d.setPosition(new Vec4(currentCamPosition.x, newCamPositionCam.y));
		}
		else if(e.isShiftDown()){
			this.view.setFovWidth(this.view.getFovWidtht() * zoomFac);
			this.view.setFovHeight(this.view.getFovHeight() * zoomFac);
		}
		else{
			this.view2d.setFovWidth(fovWidthNew);
			this.view2d.setFovHeight(fovHeightNew);
			this.view2d.setPosition(newCamPositionCam);
		}
		this.wd.redraw();
	}
}
