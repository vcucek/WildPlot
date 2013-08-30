package si.wildplot.core.render;

import si.wildplot.common.util.IntegrateProperies;
import si.wildplot.common.util.SpecialParameter;
import si.wildplot.core.jocl.BasicCLContext;

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
