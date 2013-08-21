package mafi.common.util;

import mafi.common.math.Matrix;
import mafi.common.math.Vec4;
import mafi.core.DrawContext;

/**
 *
 * @author vito
 */
public class ViewUtil {

	public static Vec4 calcCameraPosition(DrawContext dc, Vec4 screenPosition){

		double width = (double)(dc.getDrawableWidth());
		double height = (double)(dc.getDrawableHeight());

		double near = dc.getView().getFovNear();
		double far = dc.getView().getFovFar();

		double a = far / ( far - near );
		double b = far * near / ( near - far );

		double xC = 2.0 * screenPosition.x/width;
		double yC = 2.0 * screenPosition.y/height;
//		double zC = near + (far - near) * screenPosition.z;
//		double zC = b/(screenPosition.z - a);
		double zC = 2.0 * screenPosition.z - 1.0;

		xC = xC - 1.0d;
		yC = 1.0d - yC;

		return (new Vec4(xC, yC, zC, 1.0d));
	}

	public static Vec4 calcWorldPosition(DrawContext dc, Matrix modelViewProjInverse, Vec4 screenPosition){

		double width = (double)(dc.getDrawableWidth());
		double height = (double)(dc.getDrawableHeight());

//		double near = dc.getView().getFovNear();
//		double far = dc.getView().getFovFar();

//		double a = far / ( far - near );
//		double b = far * near / ( near - far );

		double xC = 2.0 * screenPosition.x/width;
		double yC = 2.0 * screenPosition.y/height;
		double zC = 2.0 * screenPosition.z - 1.0;

		xC = xC - 1.0d;
		yC = 1.0d - yC;

		Vec4 camera = new Vec4(xC, yC, zC, 1.0d);
		Vec4 worldVec = camera.transformBy4(modelViewProjInverse);
		worldVec = worldVec.multiply3(1.0d/worldVec.w);
		return worldVec;
	}

}
