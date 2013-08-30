package si.wildplot.core.render;

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
public class Spring {
	public final double springK;
	public final double springB;
	public final double springLength;

	public Spring(double springK, double springB)
	{
		this.springK = springK;
		this.springB = springB;
		this.springLength = -1.0;
	}

	public Spring(double springK, double springB, double springLength)
	{
		this.springK = springK;
		this.springB = springB;
		this.springLength = springLength;
	}
}
