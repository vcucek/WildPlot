package si.wildplot.common.util;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
import javax.media.opengl.GL;
import javax.media.opengl.GLProfile;
import si.wildplot.core.DrawContext;

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
public class RandomTexture {

	private final int width;
	private final int height;

	private Texture texture;

	public RandomTexture(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.texture = null;
	}

	public void create(DrawContext dc){
		GL gl = dc.getGL();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				image.setRGB(i, j, (int)(Math.random()*Integer.MAX_VALUE));
			}
		}
		texture = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
	}

	public void bind(DrawContext dc){
		GL gl = dc.getGL();
		
		if(this.texture == null){
			create(dc);
		}

		if(this.texture != null){
			texture.enable(gl);
			texture.bind(gl);
		}
	}

	public void dispose(DrawContext dc){
		GL gl = dc.getGL();
		
		if(this.texture != null){
			texture.disable(gl);
			texture.destroy(gl);
		}
	}

}
