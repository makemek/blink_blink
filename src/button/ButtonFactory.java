package button;

import processing.core.PApplet;
import button.primitive.Button;
import button.symbol.Symbol;

public class ButtonFactory {
	
	public static Switch createSwitch(Button bt, final Symbol[] on, final Symbol[] off)
	{
		Symbol onSym = new Symbol() {

			@Override
			public void draw(PApplet applet, float posX, float posY) {
				for (Symbol s : on)
					s.draw(applet, posX, posY);
			}
		};
		
		Symbol offSym = new Symbol() {

			@Override
			public void draw(PApplet applet, float posX, float posY) {
				for (Symbol s : off)
					s.draw(applet, posX, posY);
			}
			
		};
		
		return new Switch(bt, onSym, offSym);
		
	}
}
