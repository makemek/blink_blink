package textbox;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PShape;

public class TextBox {
	private PApplet applet;
	private PShape box = new PShape();
	private int textSize = 25;
	private Color textColor = Color.WHITE;
	public String text = "";

	public TextBox(PApplet applet, float boxWidth, float boxHeight) {
		this.applet = applet;
		this.text = text;
		
		box = applet.createShape(PApplet.RECT,0,0,boxWidth,boxHeight);
		box.setFill(false);
		box.beginShape(PApplet.RECT);
		box.strokeWeight(2);
		box.endShape(PApplet.RECT);
		
		try {
		applet.textFont(applet.createFont("AngsanaUPC-Bold-48.vlw", 50));
		} catch(Exception e) {
			System.err.println(e);
			
		}
		
		setTextSize(textSize);
		setStrokeColor(Color.GRAY);
		setStrokeWeight(4);
	}

	public void draw(float posX, float posY) {
		setTextSize(textSize);
		applet.translate(posX, posY);
		applet.fill(textColor.getRGB());
		applet.text(text, 0, box.getHeight()/4, box.getWidth(), box.getHeight());
		applet.textAlign(PApplet.CENTER);
		applet.shape(box, 0, 0);
		applet.translate(-posX, -posY);
	}
	
	public void setTextSize(int textSize) {
		this.textSize = textSize;
		applet.textSize(this.textSize);
	}
	
	public void setStrokeColor(Color _color) {
		box.beginShape(PApplet.RECT);
		box.stroke(_color.getRGB());
		box.endShape();
	}
	
	public void setStrokeWeight(float weight) {
		box.beginShape(PApplet.RECT);
		box.strokeWeight(weight);
		box.endShape();
	}
	
	public void showBox(boolean show) {
		box.setVisible(show);
	}
	
	public void setTextColor(Color _color) {
		textColor = _color;
	}
	
}
