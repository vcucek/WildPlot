package si.wildplot.common.util;

import si.wildplot.common.math.Matrix;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.DrawContext;

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
