package si.wildplot.core.render;

import si.wildplot.common.util.IntegrateProperies;
import si.wildplot.common.util.SpecialParameter;
import si.wildplot.core.jocl.BasicCLContext;

/**
 *
 * @author vito
 */
public interface Function extends Renderable{

	public String getFunction();
	public void setFunction(String function);
	public int getType();

	public SpecialParameter[] getSpecialParameters();

	public boolean isEnabled();
	public void setEnabled(boolean enable);

	//mathematic operations
	public String integrate(BasicCLContext context, IntegrateProperies properties);
	
}
