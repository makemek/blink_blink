package button;

import processing.core.PApplet;
import processing.core.PShape;
import applet.BlinkBlink;
import button.primitive.Button;
import button.symbol.Symbol;

public class ButtonFactory {
	
	public static Switch createSwitch(Button bt, final PShape[] on, final PShape[] off)
	{
		
		PShape onSym = BlinkBlink.getInstance().createShape(PApplet.GROUP);
		PShape offSym = BlinkBlink.getInstance().createShape(PApplet.GROUP);
		
		for (PShape p : on)
			onSym.addChild(p);
		
		for (PShape p : off)
			offSym.addChild(p);
		
		return new Switch(bt, onSym, offSym);
		
	}
}
