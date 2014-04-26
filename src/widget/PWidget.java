package widget;

import helper.Drawable;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import processing.core.PShape;
import applet.BlinkBlink;

public abstract class PWidget implements Drawable
{
	protected BlinkBlink applet = BlinkBlink.getInstance();
	public Point pos;
	public Dimension dim;
	
	protected ArrayList<PShape> shapes = new ArrayList<>();
	
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