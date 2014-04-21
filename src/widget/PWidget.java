package widget;

import processing.core.PApplet;

public interface PWidget
{
	abstract void draw(PApplet applet, float posX, float posY, float width, float height);
}