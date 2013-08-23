package si.wildplot.core.render;

import si.wildplot.common.util.IntegrateProperies;
import si.wildplot.common.util.SpecialParameter;
import si.wildplot.core.jocl.BasicCLContext;

/**
 *
 * @author vito
 */
public interface Function extends Renderable{

	public static final int TYPE_2D_EXPLICIT = 1;
	public static final int TYPE_2D_IMPLICIT = 2;
	public static final int TYPE_3D_EXPLICIT = 3;
	
	public String getFunction();
	public void setFunction(String function);
	public int getType();

	public SpecialParameter[] getSpecialParameters();

	public boolean isEnabled();
	public void setEnabled(boolean enable);

	//mathematic operations
	public String integrate(BasicCLContext context, IntegrateProperies properties);
	
}
