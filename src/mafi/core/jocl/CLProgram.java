package mafi.core.jocl;

import java.util.HashMap;
import mafi.common.util.Logging;
import static org.jocl.CL.*;
import org.jocl.*;

/**
 *
 * @author vito
 */
public class CLProgram {

	private final cl_program program;
	private HashMap<String,cl_kernel> kernels = new HashMap<String, cl_kernel>();

	public CLProgram(cl_program program)
	{
		this.program = program;
	}

	public cl_kernel getKernel(String name){
		if(this.program == null){
			Logging.logger().severe("CLProgram.nullPointerException");
			throw new IllegalStateException(Logging.getMessage("CLProgram.nullPointerException"));
		}
		if(kernels.get(name) != null){
			return kernels.get(name);
		}
        cl_kernel kernelOut = clCreateKernel(program, name, null);
		this.kernels.put(name, kernelOut);
		return kernelOut;
	}

	public void dispose(){
		for(cl_kernel kernel : kernels.values()){
			if(kernel != null){
				clReleaseKernel(kernel);
			}
		}
		kernels.clear();
		if(this.program != null){
        	clReleaseProgram(this.program);
		}
	}
}
