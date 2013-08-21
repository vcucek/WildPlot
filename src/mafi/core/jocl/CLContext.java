package mafi.core.jocl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import mafi.common.util.Logging;
import static org.jocl.CL.*;
import org.jocl.*;

/**
 *
 * @author vito
 */

public class CLContext
{
	private cl_context clContext = null;
	private cl_device_id devices[] = null;
	private cl_command_queue clCommandQueue = null;

	public CLContext()
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

        long numBytes[] = new long[1];

        // Obtain the platform IDs and initialize the context properties
        System.out.println("Obtaining platform...");
        cl_platform_id platforms[] = new cl_platform_id[1];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platforms[0]);

        // Create an OpenCL context on a GPU device
        this.clContext = clCreateContextFromType(
            contextProperties, CL_DEVICE_TYPE_GPU, null, null, null);
        if (this.clContext == null)
        {
            // If no context for a GPU device could be created,
            // try to create one for a CPU device.
            this.clContext = clCreateContextFromType(
                contextProperties, CL_DEVICE_TYPE_CPU, null, null, null);

            if (this.clContext == null)
            {
                System.out.println("Unable to create a context");
                return;
            }
        }

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Get the list of GPU devices associated with the context
        clGetContextInfo(this.clContext, CL_CONTEXT_DEVICES, 0, null, numBytes);

        // Obtain the cl_device_id for the first device
        int numDevices = (int) numBytes[0] / Sizeof.cl_device_id;
        this.devices = new cl_device_id[numDevices];
        clGetContextInfo(this.clContext, CL_CONTEXT_DEVICES, numBytes[0],
            Pointer.to(devices), null);
	}

	private void createCommandQueue(){
		// Create a command-queue
		this.clCommandQueue = clCreateCommandQueue(this.clContext, devices[0], 0, null);
	}

	public cl_context getContext(){
		if(this.clContext == null){
			Logging.logger().severe("CLContext.nullPointerException:contextIsNull");
			throw new IllegalStateException(Logging.getMessage("CLContext.nullPointerException:contextIsNull"));
		}

		return clContext;
	}

	public cl_command_queue getCommandQueue(){
		if(this.clCommandQueue == null){
			Logging.logger().severe("CLContext.nullPointerException:clcommandQueueIsNull");
			throw new IllegalStateException(Logging.getMessage("CLContext.nullPointerException:clcommandQueueIsNull"));
		}

		return this.clCommandQueue;
	}

	public CLProgram compileProgramFile(String filename){
		// Create the program from the source code
		String source = loadFile(filename);
		cl_program program = clCreateProgramWithSource(this.clContext,
			1, new String[]{ source }, null, null);

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);
		CLProgram programObject = new CLProgram(program);
		return programObject;
	}

	public CLProgram compileProgramSource(String source){
		// Create the program from the source code
		cl_program program = clCreateProgramWithSource(this.clContext,
			1, new String[]{ source }, null, null);

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);
		CLProgram programObject = new CLProgram(program);
		return programObject;
	}

	public void dispose(){
       	clReleaseCommandQueue(this.clCommandQueue);
       	clReleaseContext(this.clContext);
	}
}
