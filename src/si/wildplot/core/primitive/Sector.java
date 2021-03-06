package si.wildplot.core.primitive;

import si.wildplot.common.math.Matrix;
import si.wildplot.common.math.Vec4;
import si.wildplot.common.util.Logging;

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
public class Sector {

	public double minX;
	public double minY;
	public double maxX;
	public double maxY;

	private double centerX;
	private double centerY;

	public static Sector EMPTY_SECTOR = new Sector(Vec4.ZERO, Vec4.ZERO);

	public Sector(double minX, double minY, double maxX, double maxY){

		if(minX > maxX || minY > maxY){
			String msg = Logging.getMessage("Sector.minValueLargerThenMax");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
		}

		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;

		this.centerX = (maxX + minX) / 2.0d;
		this.centerY = (maxY + minY) / 2.0d;
	}

	public Sector(Vec4 min, Vec4 max){
		this(min.x, min.y, max.x, max.y);
	}

	public Sector(Vec4 center, double width, double height){
		this(center.x - width/2.0d, center.y - height/2.0d,
				center.x + width/2.0d, center.y + width/2.0d);
	}

	public Sector(Sector sector){
		this(sector.minX, sector.minY, sector.maxX, sector.maxY);
	}

	public double getWidth(){
		return (this.maxX - this.minX);
	}

	public double getHeight(){
		return (this.maxY - this.minY);
	}

	public Vec4 getCenter(){
		return new Vec4(centerX, centerY);
	}

	public Sector transform(Matrix m){
		Vec4 min = new Vec4(minX, minY);
		Vec4 max = new Vec4(maxX, maxY);

		return (new Sector(min.transformBy4(m), max.transformBy4(m)));
	}

}
