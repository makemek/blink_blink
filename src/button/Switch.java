package button;

import helper.Drawable;
import helper.Switchable;
import processing.core.PShape;
import button.primitive.Button;
import button.primitive.ButtonEvent;
import button.symbol.Symbol;

public class Switch implements Drawable
{
	private boolean on = false;
	protected final Button button;
	
	private PShape onSym = null, offSym = null;
		
	public void toggle()
	{
		if(on)
			off();
		else
			on();
	}
	
	public Switch(final Button bt)
	{
		this.button = bt;
		off();
	}
	
	public Switch(final Button bt, PShape on, PShape off) {
		this.button = bt;
		this.onSym = on;
		this.offSym = off;
		off();
	}
	
	public Button getButton() {return button;}
	
	public void setOnSymbol(PShape symbol) {
		this.onSym = symbol;
	}
	
	public void setOffSymbol(PShape symbol) {
		this.offSym = symbol;
	}
	
	public boolean isEnable() {return on;}
	
	public void addEventListener(final Switchable event) {
		
		button.register(new ButtonEvent() {

			@Override
			public void onClicked() 
			{
				if(!on)
				{
					event.enable();
					on();
				}

				else
				{
					event.disable();
					off();
				}
				
			}
		});
		
	}
	
	public void on()
	{
		on = true;
		if(onSym != null)
			button.setSymbol(onSym);
	}
	
	public void off()
	{
		on = false;
		if(offSym != null)
			button.setSymbol(offSym);
	}
	
	@Override
	public void draw() {
		button.draw();
		
	}
}

