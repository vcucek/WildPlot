package si.wildplot.core.render;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;
import java.awt.Point;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Vec4;
import si.wildplot.common.util.ViewUtil;
import si.wildplot.core.DrawContext;
import si.wildplot.core.MafiObjectImpl;
import si.wildplot.core.event.SelectEvent;

/**
 *
 * @author vito
 */
public class CoordinateSystem3D extends MafiObjectImpl implements Renderable{

	private TextRenderer textRenderer;
	private DecimalFormat decSciFormat;
	private DecimalFormat decDigFormat;
	private ArrayList<TextNumber> textListX;
	private ArrayList<TextNumber> textListY;

	public CoordinateSystem3D(){
		textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 10));
		decSciFormat = new DecimalFormat("#.######E0###");
		decDigFormat = new DecimalFormat("###0.####");
		textListX = new ArrayList<TextNumber>();
		textListY = new ArrayList<TextNumber>();

	}

	private Double round(Double num){
		String strdY = num.toString();
		boolean round = false;
		boolean stopRound = false;
		String strdYnew = "";
		for(int i=0; i<strdY.length(); i++){
			char c = strdY.charAt(i);
			if(c == '.'){
				strdYnew += c;
				continue;
			}
			if(round && !stopRound){
				if(c == 'e' || c == 'E' || c == '-'){
					stopRound = true;
				}
				else{
					strdYnew += '0';
					continue;
				}
			}
			if(c != '0' && !stopRound){
				round = true;
			}
			strdYnew += c;
		}
		return Double.parseDouble(strdYnew);
	}

	private void renderTextX(DrawContext dc, String s, Vec4 position, int xOffset, int yOffset){
		textRenderer.beginRendering(dc.getDrawableWidth(), dc.getDrawableHeight());
		textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.8f);
		Vec4 textPosition = position.transformBy4(dc.getView().getModelViewProjection());
		textPosition = textPosition.add3(new Vec4(1.0,1.0)).divide3(2.0);

		int xPos = (int)(textPosition.x * dc.getDrawableWidth());
		int yPos = (int)(textPosition.y * dc.getDrawableHeight());

		xPos = Math.max(xPos, 0);
		xPos = Math.min(xPos, dc.getDrawableWidth()- 100);

		xPos += xOffset;
		yPos += yOffset;

		textRenderer.draw(s, xPos, yPos);
		textRenderer.endRendering();
	}

	private void renderTextY(DrawContext dc, String s, Vec4 position, int xOffset, int yOffset){
		textRenderer.beginRendering(dc.getDrawableWidth(), dc.getDrawableHeight());
		textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.8f);
		Vec4 textPosition = position.transformBy4(dc.getView().getModelViewProjection());
		textPosition = textPosition.add3(new Vec4(1.0,1.0)).divide3(2.0);

		int xPos = (int)(textPosition.x * dc.getDrawableWidth());
		int yPos = (int)(textPosition.y * dc.getDrawableHeight());

		yPos = Math.max(yPos, 30);
		yPos = Math.min(yPos, dc.getDrawableHeight()- 15);

		xPos += xOffset;
		yPos += yOffset;

		textRenderer.draw(s, xPos, yPos);
		textRenderer.endRendering();
	}

	public void preRender(DrawContext dc)
	{
	}

	public void pick(DrawContext dc)
	{
	}

	public void render(DrawContext dc)
	{
		GL2 gl = dc.getGL().getGL2();
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		//////////////////render mouse coords and box/////////////////////
		Point mousePoint = dc.getView().getInputHandler().getMousePoint();
		FloatBuffer depth = Buffers.newDirectFloatBuffer(1);
		gl.glReadPixels(mousePoint.x, dc.getDrawableHeight() - mousePoint.y, 1, 1, GL2.GL_DEPTH_COMPONENT, GL.GL_FLOAT, depth);
//			Vec4 mouseVec = ViewUtil.calcCameraPosition(dc, new Vec4(mousePoint.x, mousePoint.y, depth.get(0)));
		depth.clear();
		Vec4 mouseWorldVec = ViewUtil.calcWorldPosition(dc,
				dc.getView().getModelViewProjectionI(),
				new Vec4(mousePoint.x, mousePoint.y, depth.get(0)));

		//coordsys at mouse point
		gl.glBegin(GL.GL_LINES);
		gl.glColor4f(0.5f, 0.5f, 1.0f, 0.8f);
		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y, mouseWorldVec.z + 0.05f);
		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y, mouseWorldVec.z - 0.05f);
		gl.glColor4f(0.5f, 1.0f, 0.5f, 0.8f);
		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y + 0.05f, mouseWorldVec.z);
		gl.glVertex3d(mouseWorldVec.x, mouseWorldVec.y - 0.05f, mouseWorldVec.z);
		gl.glColor4f(1.0f, 0.5f, 0.5f, 0.8f);
		gl.glVertex3d(mouseWorldVec.x + 0.05f, mouseWorldVec.y, mouseWorldVec.z);
		gl.glVertex3d(mouseWorldVec.x - 0.05f, mouseWorldVec.y, mouseWorldVec.z);
		gl.glEnd();
		//

		//render planes
		if(!dc.getView().getInputHandler().isAnimate() && mouseWorldVec.z >= -0.5 && mouseWorldVec.y <= 0.5){
			gl.glBegin(GL2.GL_POLYGON);
			gl.glColor4f(0.5f, 0.5f, 1.0f, 0.3f);
			gl.glVertex3f(-0.5f, -0.5f, (float)mouseWorldVec.z);
			gl.glVertex3f(0.5f, -0.5f, (float)mouseWorldVec.z);
			gl.glVertex3f(0.5f, 0.5f, (float)mouseWorldVec.z);
			gl.glVertex3f(-0.5f, 0.5f, (float)mouseWorldVec.z);
			gl.glEnd();
//				gl.glBegin(GL.GL_POLYGON);
//				gl.glColor4f(1.0f, 0.5f, 0.5f, 0.3f);
//				gl.glVertex3f((float)mouseWorldVec.x, -0.5f, -0.5f);
//				gl.glVertex3f((float)mouseWorldVec.x, 0.5f, -0.5f);
//				gl.glVertex3f((float)mouseWorldVec.x, 0.5f, 0.5f);
//				gl.glVertex3f((float)mouseWorldVec.x, -0.5f, 0.5f);
//				gl.glEnd();
//				gl.glBegin(GL.GL_POLYGON);
//				gl.glColor4f(0.5f, 1.0f, 0.5f, 0.3f);
//				gl.glVertex3f(-0.5f, (float)mouseWorldVec.y, -0.5f);
//				gl.glVertex3f(0.5f, (float)mouseWorldVec.y, -0.5f);
//				gl.glVertex3f(0.5f, (float)mouseWorldVec.y, 0.5f);
//				gl.glVertex3f(-0.5f, (float)mouseWorldVec.y, 0.5f);
//				gl.glEnd();
		}

		//render box
		gl.glBegin(GL.GL_LINES);
		gl.glColor4f(0.5f, 0.5f, 1.0f, 0.5f);
		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
		gl.glVertex3f(0.5f, 0.5f, -0.5f);
		gl.glVertex3f(0.5f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		gl.glVertex3f(0.5f, -0.5f, 0.5f);
		gl.glVertex3f(0.5f, -0.5f, -0.5f);

		gl.glColor4f(1.0f, 0.5f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
		gl.glVertex3f(0.5f, -0.5f, -0.5f);
		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		gl.glVertex3f(0.5f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
		gl.glVertex3f(0.5f, 0.5f, -0.5f);
		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
		gl.glVertex3f(0.5f, -0.5f, 0.5f);

		gl.glColor4f(0.5f, 1.0f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
		gl.glVertex3f(0.5f, -0.5f, 0.5f);
		gl.glVertex3f(0.5f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		gl.glVertex3f(0.5f, -0.5f, -0.5f);
		gl.glVertex3f(0.5f, 0.5f, -0.5f);
		gl.glEnd();

		gl.glEnable(GL.GL_TEXTURE_2D);

		dc.getView().setMatrixMode(gl, GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		dc.getView().setMatrixMode(gl, GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glActiveTexture(GL.GL_TEXTURE0);
		textRenderer.beginRendering(dc.getDrawableWidth(), dc.getDrawableHeight(), true);
		textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.8f);
		textRenderer.draw("moj tekst", mousePoint.x, dc.getDrawableHeight() - mousePoint.y);
		textRenderer.flush();
		textRenderer.endRendering();

		dc.getView().setMatrixMode(gl, GL2.GL_PROJECTION);
		gl.glPopMatrix();
		dc.getView().setMatrixMode(gl, GL2.GL_MODELVIEW);
		gl.glPopMatrix();
		//////////////////////////////////////////////////////////

		gl.glDisable(GL.GL_BLEND);
		gl.glDisable(GL.GL_TEXTURE_2D);
	}

	public void dispose(DrawContext dc)
	{
	}

	public boolean isPickEnabled()
	{
		return false;
	}

	public void selected(SelectEvent event)
	{
	}

	private class TextNumber{
		public String s;
		public Vec4 position;

		public TextNumber(String s, Vec4 position){
			this.s = s;
			this.position = position;
		}
	}
}
