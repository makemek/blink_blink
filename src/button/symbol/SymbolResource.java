package button.symbol;

import java.awt.Color;

import applet.BlinkBlink;
import processing.core.PApplet;
import processing.core.PShape;

public class SymbolResource {
	
	public static final byte WIDTH = 50;
	public static final byte HEIGHT = 50;
	
	private static PApplet applet = BlinkBlink.getInstance();
	
	public static PShape primitive(int kind, float width, float height)
	{
		PShape s = applet.createShape(kind, 0, 0, width, height);
		setDimension(s);
		return s;
	}
	
	public static PShape primitive(int kind)
	{
		return primitive(kind, WIDTH, HEIGHT);
	}
	
	public static PShape pauseSymbol()
	{
		final int GAP = WIDTH >> 2;
		final int RECT_WIDTH = (WIDTH - GAP) >> 1;
		final int COLOR = Color.white.getRGB();
		
		PShape bar1 = applet.createShape(PApplet.RECT, 0, 0, RECT_WIDTH, HEIGHT);
		PShape bar2 = applet.createShape(PApplet.RECT, RECT_WIDTH + GAP, 0, RECT_WIDTH, HEIGHT);
		
		PShape shape = applet.createShape(PApplet.GROUP);
		shape.addChild(bar1);
		shape.addChild(bar2);
		
		setDimension(shape);
		
		return shape;
	}
	
	public static PShape playSymbol()
	{
		final int OFFSET_X = 5;
		PShape shape = applet.createShape();
		shape.beginShape(PApplet.TRIANGLE);
		shape.vertex(OFFSET_X, 0);
		shape.vertex(OFFSET_X, HEIGHT);
		shape.vertex(OFFSET_X + WIDTH, HEIGHT >> 1);
		shape.endShape(PApplet.CLOSE);
		
		setDimension(shape);
		
		return shape;
	}
	
	public static PShape loopSymbol()
	{
		byte halfW = WIDTH >> 1, halfH = HEIGHT >> 1;
		PShape shape = applet.createShape(PApplet.ARC, halfW, halfH, halfW, halfH, -PApplet.HALF_PI, PApplet.PI);
//		shape.beginShape();
//		shape.noFill();
//		shape.stroke(0);
//		shape.strokeWeight(4);
//		shape.endShape();
		
		setDimension(shape);
		
		return shape;
	}
	
	public static PShape crossSymbol()
	{
		PShape shape = applet.createShape(PApplet.LINE, 0, HEIGHT, WIDTH, 0);

		setDimension(shape);
		return shape;
	}
	
	public static PShape muteSymbol()
	{
		PShape shape = applet.createShape(PApplet.GROUP);
		
		PShape triangle = applet.createShape();
		triangle.beginShape();
		triangle.vertex(0, 25);
		triangle.vertex(WIDTH, 0);
		triangle.vertex(WIDTH, HEIGHT);
		triangle.noStroke();
		triangle.endShape(PApplet.CLOSE);
		
		PShape rect = applet.createShape(PApplet.RECT, 0, 0, 20, HEIGHT);
		
		shape.addChild(rect);
		shape.addChild(triangle);
		
		//shape.fill(0);
		
		setDimension(shape);
		
		return shape;
		
	}
	
	private static void setDimension(PShape shape)
	{
		shape.width = WIDTH;
		shape.height = HEIGHT;
	}
}
