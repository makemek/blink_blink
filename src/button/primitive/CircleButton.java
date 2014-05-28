package button.primitive;

import java.awt.Color;
import java.awt.event.MouseEvent;

import button.symbol.Symbol;
import processing.core.PApplet;
import processing.core.PShape;

public class CircleButton extends Button {
	
	protected float diameter;
	
			
	public CircleButton(float posX, float posY, float diameter)
	{
		super();

		this.posX = posX;
		this.posY = posY;
		
		this.diameter = diameter;
		btShape = Symbol.primitive(PApplet.ELLIPSE, diameter, diameter);
	}

	public CircleButton(float diameter)
	{
		this(0f,0f,diameter);
	}
	
	protected void circle()
	{
		applet.ellipseMode(applet.RADIUS);
		applet.fill(_color.getRGB());
		applet.strokeWeight(3);
		applet.stroke(128);
		applet.ellipse(posX, posY, diameter, diameter);
	}
	
	public void drawShape()
	{
//		circle();
		applet.shape(btShape, posX, posY);
		drawSymbol();
	}
	
	protected void drawSymbol()
	{
		if(logo == null) return;
//		applet.pushMatrix();
//		
//		applet.translate(posX + diameter/4, posY + diameter/4);
//		applet.scale(diameter/logo.width/2, diameter/logo.height/2);
//		applet.translate(-posX, -posY);
//		
//		applet.translate(posX -logo.width/2f, posY -logo.height/2f);
		float rad = diameter/2;
		float hRad = rad/2;
		applet.shape(logo, posX + hRad, posY + hRad, rad, rad);
//		
//		applet.popMatrix();
		
	}
	
	
	
	public boolean isHover(int x, int y) {
		
		return ( (x > posX) && (x < posX + diameter) &&
				 (y > posY) && (y < posY + diameter));
	}
	
	public float getdiameter() {return diameter;}
	
	protected void scaleUp(final float SCALE)
	{
		float radius = diameter/2;
		applet.pushMatrix();
		applet.translate(posX + radius, posY + radius);
		applet.scale(SCALE);
		applet.translate(-posX - radius, -posY - radius);
		drawShape();
		applet.popMatrix();
	}
	
	@Override
	public void onHover() {
		scaleUp(1.25f);
		
	}
	
	@Override
	public void onPressed() {
		scaleUp(0.85f);
		
	}
	
}

