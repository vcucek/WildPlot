package si.wildplot.core.render;

import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.common.math.Vec4;
import si.wildplot.core.DrawContext;
import si.wildplot.core.MafiObjectImpl;
import si.wildplot.core.event.SelectEvent;
import si.wildplot.core.primitive.Line;

/**
 *
 * @author vito
 */
public class CoordinateSystem2D extends MafiObjectImpl implements Renderable{

	private TextRenderer textRenderer;
	private DecimalFormat decSciFormat;
	private DecimalFormat decDigFormat;
	private ArrayList<TextNumber> textListX;
	private ArrayList<TextNumber> textListY;

	public CoordinateSystem2D(){
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
		int xN = dc.getDrawableWidth()/5;
		int yN = dc.getDrawableHeight()/5;

		double VSwidth = dc.getView().getVisibleSector().getWidth();
		double VSheight = dc.getView().getVisibleSector().getHeight();
		double VSminX = dc.getView().getVisibleSector().minX;
		double VSmaxX = dc.getView().getVisibleSector().maxX;
		double VSminY = dc.getView().getVisibleSector().minY;
		double VSmaxY = dc.getView().getVisibleSector().maxY;

		Double dX = VSwidth/xN;
		Double dY = VSheight/yN;

		dX = round(dX);
		dY = round(dY);

		xN = (int)(VSwidth/dX);
		yN = (int)(VSheight/dY);

		double tX = (dc.getView().getVisibleSector().minX/dX);
		double tY = (dc.getView().getVisibleSector().minY/dY);

		int facX = (int)tX;
		int facY = (int)tY;

		gl.glDisable(GL.GL_LINE_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		dc.getView().setMatrixMode(gl, GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		dc.getView().setMatrixMode(gl, GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glBegin(GL.GL_LINES);
		///////////////render X lines///////////////////

		//render mainX "abscisa"
		gl.glColor4f(1.0f, 0.4f, 0.4f, 0.6f);
		Line mainX = new Line(new Vec4(VSminX, 0), new Vec4(VSmaxX, 0));
		mainX = mainX.transform(dc.getView().getModelViewProjection());
		gl.glVertex2d(mainX.getStart().x, mainX.getStart().y);
		gl.glVertex2d(mainX.getEnd().x, mainX.getEnd().y);

		for(int i=0; i<yN; i++){
			double lineYPos = (facY + i) * dY;

			//text assembly
			if((facY+i) % 10 == 0){
				gl.glColor4f(1.0f, 0.4f, 0.4f, 0.2f);
				String number;
				if(VSheight >= 1000 || VSheight <= 0.001 ){
					number = decSciFormat.format(lineYPos);
				}
				else{
					number = decDigFormat.format(lineYPos);
				}
				Vec4 position = new Vec4(0, lineYPos);
				textListX.add(new TextNumber(number, position));
			}
			else{
				gl.glColor4f(1.0f, 0.4f, 0.4f, 0.1f);
			}

			//draw line
			Line lineX = new Line(new Vec4(VSminX, lineYPos), new Vec4(VSmaxX, lineYPos));
			lineX = lineX.transform(dc.getView().getModelViewProjection());
			gl.glVertex2d(lineX.getStart().x, lineX.getStart().y);
			gl.glVertex2d(lineX.getEnd().x, lineX.getEnd().y);
		}
		///////////////render Y lines///////////////////

		//render mainY "ordinata"
		gl.glColor4f(0.4f, 1.0f, 0.4f, 0.6f);
		Line mainY = new Line(new Vec4(0, VSminY), new Vec4(0, VSmaxY));
		mainY = mainY.transform(dc.getView().getModelViewProjection());
		gl.glVertex2d(mainY.getStart().x, mainY.getStart().y);
		gl.glVertex2d(mainY.getEnd().x, mainY.getEnd().y);

		for(int i=0; i<xN; i++){
			double lineXPos = (facX + i) * dX;

			//text assembly
			if((facX+i) % 10 == 0){
				gl.glColor4f(0.4f, 1.0f, 0.4f, 0.1f);
				String number;
				if(VSwidth >= 1000 || VSwidth <= 0.001 ){
					number = decSciFormat.format(lineXPos);
				}
				else{
					number = decDigFormat.format(lineXPos);
				}
				Vec4 position = new Vec4(lineXPos, 0);
				textListY.add(new TextNumber(number, position));
			}
			else{
				gl.glColor4f(0.4f, 1.0f, 0.4f, 0.05f);
			}

			//draw line
			Line lineY = new Line(new Vec4(lineXPos, VSminY), new Vec4(lineXPos, VSmaxY));
			lineY = lineY.transform(dc.getView().getModelViewProjection());
			gl.glVertex2d(lineY.getStart().x, lineY.getStart().y);
			gl.glVertex2d(lineY.getEnd().x, lineY.getEnd().y);
		}
		gl.glEnd();

		//render text
		int i=2;
		for(TextNumber tn : textListY){
			renderTextY(dc, tn.s, tn.position, 0, -10 * (i%2));
			i+=1;
		}
		textListY.clear();

		for(TextNumber tn : textListX){
			renderTextX(dc, tn.s, tn.position,0,0);
		}
		textListX.clear();

		dc.getView().setMatrixMode(gl, GL2.GL_PROJECTION);
		gl.glPopMatrix();
		dc.getView().setMatrixMode(gl, GL2.GL_MODELVIEW);
		gl.glPopMatrix();

		gl.glDisable(GL.GL_BLEND);
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
