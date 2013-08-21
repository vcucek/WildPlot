package mafi.common.util;

/**
 *
 * @author vito
 */
public class SpecialParameter{

	public final char name;
	private double value = 1.0d;

	public SpecialParameter(char name)
	{
		this.name = name;
	}
	public double getValue(){
		return value;
	}
	public void setValue(double value){
		this.value = value;
	}
}