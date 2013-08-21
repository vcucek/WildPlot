package mafi.core.primitive;

import mafi.common.math.Matrix;
import mafi.common.math.Vec4;

/**
 *
 * @author vito
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
