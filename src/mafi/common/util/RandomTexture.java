package mafi.common.util;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.image.BufferedImage;
import javax.media.opengl.GL;
import mafi.core.DrawContext;

/**
 *
 * @author vito
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
		texture = TextureIO.newTexture(image, false);
	}

	public void bind(DrawContext dc){
		if(this.texture == null){
			create(dc);
		}

		if(this.texture != null){
			texture.enable();
			texture.bind();
		}
	}

	public void dispose(DrawContext dc){
		if(this.texture != null){
			texture.disable();
			texture.dispose();
		}
	}

}
