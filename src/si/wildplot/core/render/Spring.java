package si.wildplot.core.render;

/**
 *
 * @author vito
 */
public class Spring {
	public final double springK;
	public final double springB;
	public final double springLength;

	public Spring(double springK, double springB)
	{
		this.springK = springK;
		this.springB = springB;
		this.springLength = -1.0;
	}

	public Spring(double springK, double springB, double springLength)
	{
		this.springK = springK;
		this.springB = springB;
		this.springLength = springLength;
	}
}
