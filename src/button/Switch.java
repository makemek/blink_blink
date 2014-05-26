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
	
	private ButtonEvent swEvent = new ButtonEvent() {

		@Override
		public void onClicked() {
			if(!on)
				enable();
			else
				disable();
		}		
	};
	
	public Switch(final Button bt)
	{
		this.button = bt;
		disable();
		button.register(swEvent);
	}
	
	public Switch(final Button bt, PShape on, PShape off) {
		this.button = bt;
		this.onSym = on;
		this.offSym = off;
		disable();
		button.register(swEvent);
	}
	
	public Button getButton() {return button;}
	
	public void setOnSymbol(PShape symbol) {
		this.onSym = symbol;
	}
	
	public void setOffSymbol(PShape symbol) {
		this.offSym = symbol;
	}
	
	
	public boolean isEnable() {return on;}
	
	public void enable() 
	{
		on = true;
		if(onSym != null)
			button.setSymbol(onSym);
		
	}
	
	public void disable() 
	{
		on = false;
		if(offSym != null)
			button.setSymbol(offSym);
	}

	public void addEventListener(final Switchable event) {
		button.unregister(swEvent);
		
		button.register(new ButtonEvent() {

			@Override
			public void onClicked() 
			{
				if(!on)
					event.enable();

				else
					event.disable();
			}
		});
		
		button.register(swEvent); // on/off do last
	}

	@Override
	public void draw() {
		button.draw();
		
	}
}

