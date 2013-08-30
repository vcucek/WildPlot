package si.wildplot.core.jocl;

import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLContext;
import com.jogamp.opencl.CLDevice;
import com.jogamp.opencl.CLProgram;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
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

public class BasicCLContext
{
	private CLContext clContext = null;
	private CLDevice device = null;
	private CLCommandQueue clCommandQueue = null;

	public BasicCLContext()
	{
		createCLContext();
		createCommandQueue();
	}

	private String loadFile(String fileName)
	{
		String source = "";
		BufferedReader brv = null;
		brv = new BufferedReader(new InputStreamReader(this
				.getClass().getResourceAsStream(fileName)));
		String line = "";
		try
		{
			while ((line = brv.readLine()) != null)
			{
				source += (line + "\n");
			}
		}
		catch (IOException ex)
		{
			Logging.logger().log(Level.SEVERE, Logging.getMessage(
                "AbstractShader.ExceptionAttemptinToReadShader"), ex);
		}
		return source;
	}

    private void createCLContext()
    {
		if(clContext != null){
			return;
		}
		clContext = CLContext.create();

		// select fastest device
		device = clContext.getMaxFlopsDevice();
		System.out.println("using " + device);
	}

	private void createCommandQueue(){
		// Create a command-queue
		this.clCommandQueue = device.createCommandQueue();
	}

	public CLContext getContext(){
		if(this.clContext == null){
			Logging.logger().severe("CLContext.nullPointerException:contextIsNull");
			throw new IllegalStateException(Logging.getMessage("CLContext.nullPointerException:contextIsNull"));
		}

		return clContext;
	}

	public CLDevice getDevice(){
		return this.device;
	}

	public long globalWorkSizerRoundUp(long groupSize, long globalSize) {
        long r = globalSize % groupSize;
        if (r == 0) {
            return globalSize;
        } else {
            return globalSize + groupSize - r;
        }
    }

	public CLCommandQueue getCommandQueue(){
		if(this.clCommandQueue == null){
			Logging.logger().severe("CLContext.nullPointerException:clcommandQueueIsNull");
			throw new IllegalStateException(Logging.getMessage("CLContext.nullPointerException:clcommandQueueIsNull"));
		}

		return this.clCommandQueue;
	}

	public BasicCLProgram compileProgramFile(String filename){
		// Create the program from the source code
		String source = loadFile(filename);
		CLProgram program = clContext.createProgram(source);
		program.build();

		BasicCLProgram programObject = new BasicCLProgram(program);
		return programObject;
	}

	public BasicCLProgram compileProgramSource(String source){
		// Create the program from the source code
		CLProgram program = clContext.createProgram(source);
		program.build();

		// Build the program
		BasicCLProgram programObject = new BasicCLProgram(program);
		return programObject;
	}

	public void dispose(){
		clCommandQueue.release();
		clContext.release();
	}
}
