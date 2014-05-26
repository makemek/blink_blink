package arduino;

import helper.Switchable;
import processing.core.PApplet;
import button.Switch;
import button.primitive.ButtonEvent;
import cc.arduino.Arduino;

public class ArduinoController implements Switchable {
	protected Arduino arduino;
	private Switch switchBt;
	
	protected Integer[] pins;
	
	private ArduinoMaker maker;
	
	public ArduinoController(PApplet applet, final Switch switchBt)
	{
		this.switchBt = switchBt;
		this.switchBt.addEventListener(this);
		
		switchBt.getButton().register(new ButtonEvent() {

			@Override
			public void onClicked() {
				if(maker.arduino == null) {
					maker.showUI();
					switchBt.off();
				}

//				else
//				{
//					arduino = maker.arduino;
//					pins = maker.pins;	
//				}
			}
		});

		maker = new ArduinoMaker(applet);
	}
	
	public void setEnable(boolean enable) {
		if(enable) 
			enable();
		
		else
			disable();
	}
	
	public void disable()
	{
		System.out.println("DISABLE");
		if(arduino == null) return;
		//switchBt.disable();
		
		try {
		for(Integer pin : pins)
			arduino.analogWrite(pin, 0);
		} catch (Exception e) {
			System.out.println("ERROR");
			disposeArduino();
		}
		
	}
	
	public void enable() {
		System.out.println("ENABLE");
		
		arduino = maker.arduino;
		pins = maker.pins;
		
//		if(arduino == null)
//			return;
	}
	
	public void disposeArduino()
	{
		if(arduino == null)
			return;
		
		System.out.println("Disposing");
		switchBt.off();
		
		maker.arduino.dispose();
		maker.arduino = null;
		arduino = null;
		System.out.println("maker.arduino = " + maker.arduino);
		
	}
		
	public boolean isEnable() {return switchBt.isEnable();}
}