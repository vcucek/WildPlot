package si.wildplot.core.primitive;

import si.wildplot.common.math.Vec4;

/**
 *
 * @author vito
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
