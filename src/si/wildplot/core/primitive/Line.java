package si.wildplot.core.primitive;

import si.wildplot.common.math.Matrix;
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
public class Line {
	private Vec4 start;
	private Vec4 end;

	public Line(Vec4 start, Vec4 end){
		this.start = start;
		this.end = end;
	}

	public Vec4 getStart(){
		return start;
	}

	public Vec4 getEnd(){
		return end;
	}

	public Line transform(Matrix m){
		Vec4 startNew = this.start.transformBy4(m);
		Vec4 endNew = this.end.transformBy4(m);

		return new Line(startNew, endNew);
	}
}
