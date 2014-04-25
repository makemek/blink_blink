package widget;

import java.awt.Dimension;
import java.awt.Point;

import helper.Drawable;
import applet.BlinkBlink;
import processing.core.PApplet;

public abstract class PWidget implements Drawable
{
	protected BlinkBlink applet = BlinkBlink.getInstance();
	public Point pos;
	public Dimension dim;
	
	public PWidget(Point position, Dimension dim)
	{
		pos = position;
		this.dim = dim;
	}
	
	public PWidget(int posX, int posY, float width, float height)
	{
		pos = new Point(posX, posY);
		dim = new Dimension();
		dim.setSize(width, height);
	}
}