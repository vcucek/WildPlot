package si.wildplot.core.jocl;

import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLProgram;
import java.util.HashMap;
import si.wildplot.common.util.Logging;

/**
 *
 * @author vito
 */
public class BasicCLProgram {

	private final CLProgram program;
	private HashMap<String, CLKernel> kernels = new HashMap<String, CLKernel>();

	public BasicCLProgram(CLProgram program)
	{
		this.program = program;
	}

	public CLKernel getKernel(String name){
		if(this.program == null){
			Logging.logger().severe("CLProgram.nullPointerException");
			throw new IllegalStateException(Logging.getMessage("CLProgram.nullPointerException"));
		}
		if(kernels.get(name) != null){
			return kernels.get(name);
		}
        CLKernel kernelOut = program.createCLKernel(name);
		this.kernels.put(name, kernelOut);
		return kernelOut;
	}

	public void dispose(){
		for(CLKernel kernel : kernels.values()){
			if(kernel != null){
				kernel.release();
			}
		}
		kernels.clear();
		if(this.program != null){
			program.release();
		}
	}
}
