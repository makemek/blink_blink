package button.primitive;

import java.awt.Color;
import java.awt.event.MouseEvent;

import button.symbol.Symbol;
import processing.core.PApplet;

public class CircleButton extends Button {
	
	protected float radius;
	
			
	public CircleButton(float posX, float posY, float radius)
	{
		super();

		this.posX = posX;
		this.posY = posY;
		
		this.radius = radius;
		
	}

	public CircleButton(float radius)
	{
		super();
		this.radius = radius;
	}
	
	protected void circle()
	{
		applet.ellipseMode(applet.RADIUS);
		applet.fill(_color.getRGB());
		applet.strokeWeight(3);
		applet.stroke(128);
		applet.ellipse(posX, posY, radius, radius);
	}
	
	public void drawShape()
	{
		circle();
		drawSymbol();
	}
	
	protected void drawSymbol()
	{
		if(logo == null) return;
		applet.pushMatrix();
		
		applet.translate(posX, posY);
		applet.scale(radius/logo.width, radius/logo.height);
		applet.translate(-posX, -posY);
		
		applet.translate(posX -logo.width/2f, posY -logo.height/2f);
		applet.shape(logo);
		
		applet.popMatrix();
		
	}
	
	
	
	public boolean isHover(int x, int y) {
		return applet.dist(x, y, posX, posY) < radius;
	}
	
	public float getRadius() {return radius;}
	
	@Override
	public void onHover() {
		scaleUp(1.25f);
		
	}
	
	@Override
	public void onPressed() {
		scaleUp(0.85f);
		
	}
	
}

