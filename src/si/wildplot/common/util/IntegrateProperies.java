package si.wildplot.common.util;

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

public class IntegrateProperies {

	public final double min;
	public final double max;
	public final double step;
	public final int nSteps;

	public static final int LINEAR = 1;
	public static final int CUBIC = 2;
	public static final int MONTE_CARLO = 3;

	private int type;
	private boolean useGPU = true;

	public IntegrateProperies(double min, double max, double step, int type)
	{
		this.min = min;
		this.max = max;
		this.step = step;
		this.type = type;
		this.nSteps = (int)((max - min)/step);
	}

	public int getType(){
		return this.type;
	}

	public boolean calcOnGPU(){
		return useGPU;
	}
}
