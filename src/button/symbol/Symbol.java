package button.symbol;

import processing.core.PApplet;

public interface Symbol {
	byte width = 50;
	byte height = 50;
	
	void draw(PApplet applet, float posX, float posY);
}
