package mafi.core.render;

import mafi.common.util.IntegrateProperies;
import mafi.common.util.SpecialParameter;
import mafi.core.jocl.CLContext;

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
	public String integrate(CLContext context, IntegrateProperies properties);
	
}
