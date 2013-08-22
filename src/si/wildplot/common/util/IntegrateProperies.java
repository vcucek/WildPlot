package si.wildplot.common.util;

/**
 *
 * @author vito
 */
public class IntegrateProperies {

	public final double min;
	public final double max;
	public final double step;
	public final int nSteps;

	public static final int LINEAR = 1;
	public static final int CUBIC = 2;
	public static final int MONTE_CARLO = 3;

	private int type;
	private boolean useGPU = true;

	public IntegrateProperies(double min, double max, double step, int type)
	{
		this.min = min;
		this.max = max;
		this.step = step;
		this.type = type;
		this.nSteps = (int)((max - min)/step);
	}

	public int getType(){
		return this.type;
	}

	public boolean calcOnGPU(){
		return useGPU;
	}
}
