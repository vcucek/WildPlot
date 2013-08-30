package si.wildplot.core.jocl;

import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLProgram;
import java.util.HashMap;
import si.wildplot.common.util.Logging;

/*
 * (C) Copyright 2013 Vito Čuček.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * @author Vito Čuček <vito.cucek@xlab.si>
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
