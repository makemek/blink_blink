package button.primitive;

import java.awt.Color;
import java.awt.event.MouseEvent;

import button.symbol.Symbol;
import processing.core.PApplet;

public class RectButton extends Button {
	private float width, height;
	
	
	public RectButton(float posX, float posY, float width, float height)
	{
		super();
		this.applet = applet;
		
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		
		btShape = Symbol.primitive(PApplet.RECT, width, height);
	}
	
	public float getWidth() {return width;}
	public float getHeight() {return height;}
	
	public RectButton(float width, float height)
	{
		this(0f, 0f, width, height);
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
		//rectangle();
		applet.shape(btShape, posX, posY);
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
	
	public void scaleUp(final float SCALE)
	{
		applet.pushMatrix();
		
		applet.translate(posX + width/2, posY + height/2);
		applet.scale(SCALE);
		applet.translate(-posX - width/2, -posY - height/2);
		
		drawShape();
		
		applet.popMatrix();
	}
	
	@Override
	public boolean isHover(int x, int y) {
		
		return ( (x > posX) && (x < posX + width) &&
				 (y > posY) && (y < posY + height));
	}

}
