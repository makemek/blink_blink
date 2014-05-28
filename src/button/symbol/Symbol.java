package button.symbol;

import java.awt.Color;

import applet.BlinkBlink;
import processing.core.PApplet;
import processing.core.PShape;

public class Symbol {
	
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
	
	public static PShape pause()
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
	
	public static PShape play()
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
	
	public static PShape loop()
	{
		byte halfW = WIDTH >> 1, halfH = HEIGHT >> 1;
//		PShape shape = applet.createShape(PApplet.ARC, halfW, halfH, halfW, halfH, -PApplet.HALF_PI, PApplet.PI);
		PShape shape = applet.createShape(PApplet.ARC, 0, 0, WIDTH, HEIGHT, -PApplet.HALF_PI, PApplet.PI);
		shape.setFill(false);
		shape.setStrokeWeight(4);
		
		setDimension(shape);
		
		return shape;
	}
	
	public static PShape cross()
	{
		PShape shape = applet.createShape(PApplet.LINE, 0, HEIGHT, WIDTH, 0);
		shape.setStrokeWeight(4);
		shape.setStroke(Color.red.getRGB());

		setDimension(shape);
		return shape;
	}
	
	public static PShape speaker()
	{
		PShape shape = applet.createShape(PApplet.GROUP);
		
		PShape triangle = applet.createShape();
		triangle.beginShape();
		triangle.vertex(0, 25);
		triangle.vertex(WIDTH, 0);
		triangle.vertex(WIDTH, HEIGHT);
		triangle.endShape(PApplet.CLOSE);
		triangle.setFill(Color.black.getRGB());
		
		PShape rect = applet.createShape(PApplet.RECT, 0, 0, 20, HEIGHT);
		rect.setFill(Color.BLACK.getRGB());
		
		shape.addChild(triangle);
		shape.addChild(rect);
		
		
		setDimension(shape);
		
		return shape;
		
	}
	
	private static void setDimension(PShape shape)
	{
		shape.width = WIDTH;
		shape.height = HEIGHT;
	}
	
	public static PShape groupShape(PShape... group)
	{
		PShape newGroup = BlinkBlink.getInstance().createShape(PApplet.GROUP);
		
		float maxW = 0, maxH = 0;
		for(PShape p : group)
		{
			if(p.width > maxW)
				maxW = p.width;
			if(p.height > maxH)
				maxH = p.height;
			
			newGroup.addChild(p);
		}
		
		newGroup.width = maxW;
		newGroup.height = maxH;
		
		return newGroup;
	}
}
