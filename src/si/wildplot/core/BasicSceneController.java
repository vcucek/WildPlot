package si.wildplot.core;

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
public class BasicSceneController extends AbstractSceneController{

	@Override
	public void doRepaint(DrawContext dc)
	{
		this.initializeFrame(dc);
		try
		{
			this.clear(dc);
			this.applyView(dc);
			this.preRender(dc);
			this.clear(dc);
//			this.pick(dc);
//			this.clear(dc);
			this.draw(dc);
		}
		finally
		{
			this.finalizeFrame(dc);
		}
	}
}
