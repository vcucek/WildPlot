package si.wildplot.core.primitive;

import si.wildplot.common.math.Vec4;

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
public class Box {
	public final double minX;
	public final double maxX;
	public final double minY;
	public final double maxY;
	public final double minZ;
	public final double maxZ;

	public Box(double minX, double maxX, double minY, double maxY, double minZ, double maxZ)
	{
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

	public Vec4 clamp(Vec4 v){
		double x = v.x > maxX ? maxX : v.x;
		x = x < minX ? minX : x;
		double y = v.y > maxY ? maxY : v.y;
		y = y < minY ? minY : y;
		double z = v.z > maxZ ? maxZ : v.z;
		z = z < minZ ? minZ : z;
		
		return new Vec4(x, y, z);
	}
}
