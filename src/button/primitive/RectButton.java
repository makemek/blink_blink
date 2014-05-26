package button.primitive;

import java.awt.Color;
import java.awt.event.MouseEvent;

import processing.core.PApplet;

public class RectButton extends Button {
	private float width, height;
	
	
	public RectButton(float posX, float posY, float width, float height)
	{
		super();
		this.applet = applet;
		// to center point
		this.posX = posX + width/2;
		this.posY = posY + height/2;
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {return width;}
	public float getHeight() {return height;}
	
	public RectButton(float width, float height)
	{
		super();
		this.applet = applet;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void onPressed() {
		scaleUp(0.85f);	
	}

	@Override
	public void onHover() {
		scaleUp(1.25f);
	}

	private void rectangle()
	{
		applet.fill(_color.getRGB());
		applet.stroke(0, 0, 255);
		applet.strokeWeight(2);
		applet.rectMode(applet.CENTER);
		applet.rect(posX,  posY, width, height);
		applet.rectMode(applet.CORNER);
	}
	
	@Override
	public void drawShape() {
		rectangle();
		drawSymbol();
	}
		
	protected void drawSymbol()
	{
		if(logo == null) return;
		applet.pushMatrix();
		
//		applet.translate(posX, posY);
//		applet.scale(width/logo.width, height/logo.height);
//		applet.translate(-posX, -posY);
		// TODO implement
		
		if(logo.width > width || logo.height > height)
			;
		else
		{
			float x = posX - width/2, y = posY - height/2;
			applet.translate(x, y);
			applet.shape(logo);
			applet.translate(-x, -y);
		}
		
		applet.popMatrix();
	}
	
	@Override
	public boolean isHover(int x, int y) {
		final float CORN_X = posX - width/2, CORN_Y = posY - height/2;
		
		return ( (x > CORN_X) && (x < CORN_X + width) &&
				 (y > CORN_Y) && (y < CORN_Y + height));
	}

}
