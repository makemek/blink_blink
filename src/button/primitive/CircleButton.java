package button.primitive;

import java.awt.Color;
import java.awt.event.MouseEvent;

import button.symbol.Symbol;
import processing.core.PApplet;

public class CircleButton extends Button {
	
	protected float radius;
	
			
	public CircleButton(PApplet applet, float posX, float posY, float radius)
	{
		super(applet);
		this.applet = applet;

		this.posX = posX;
		this.posY = posY;
		
		this.radius = radius;
		
	}

	public CircleButton(PApplet applet, float radius)
	{
		super(applet);
		this.applet = applet;
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
		
		applet.translate(-logo.width/2f, -logo.height/2f);
		logo.draw(applet, posX ,posY);
		
		applet.popMatrix();
		
	}
	
	
	
	public boolean mouseHover() {
		return applet.dist(applet.mouseX, applet.mouseY, posX, posY) < radius;
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

