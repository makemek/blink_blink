package button.primitive;

import helper.Drawable;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PShape;
import widget.Publisher;
import applet.BlinkBlink;
import button.symbol.Symbol;

public abstract class Button extends MouseAdapter 
		implements  Publisher<ButtonEvent>, Drawable {
	
	protected PApplet applet = BlinkBlink.getInstance();
	protected float posX = 0, posY = 0;
	protected PShape logo = null;
	protected Color _color = Color.WHITE;
	
	private boolean use = true;
	protected enum STATE {IDLE, HOVER};
	protected STATE currentState = STATE.IDLE; 
	
	private ArrayList<ButtonEvent> observers = new ArrayList<ButtonEvent>();
	
	protected PShape btShape;
	protected abstract void drawShape();
	public PShape getShape() {return btShape;}
	
	public float getPositionX() {return posX;}
	public float getPositionY() {return posY;}
	public void setPositionX(float newX) {posX = newX;}
	public void setPositionY(float newY) {posY = newY;}
	public PShape getSymbol() {return logo;}
	public void setSymbol(PShape sym) {logo = sym;}
	protected abstract void drawSymbol();
	
	public abstract boolean isHover(int x, int y);
	
	public Button() {
		BlinkBlink.getInstance().addMouseListener(this);
	}
		
	public void use(boolean isEnable) {use = isEnable;}
	public boolean isUse() {return use;}
	
	protected abstract void onPressed();
	protected abstract void onHover();
	
	public void draw()
	{
		if(!use)
			return;
		
		switch(currentState)
		{
		case HOVER:
			onHover(); 
			break;

		default:
			drawShape();
		}
		
	}
			
	protected void scaleUp(final float SCALE)
	{
		applet.pushMatrix();
		
		applet.translate(posX, posY);
		applet.scale(SCALE);
		applet.translate(-posX, -posY);
		
		drawShape();
		
		applet.popMatrix();
	}

	public void register(ButtonEvent evt) {
		observers.add(evt);
	}
	
	public void unregister(ButtonEvent evt) {
		observers.remove(evt);
	}
	
	// not used
	public void notifyObserver() {
		
	}
	
	
	public void setColor(Color _color) {
		this._color = _color;
	}
	
	public void mouseEntered(MouseEvent e)
	{
		if(this.isHover(e.getX(), e.getY()))
			currentState = STATE.HOVER;
		else
			currentState = STATE.IDLE;
	}
	
	public void mouseReleased(MouseEvent e) {
		if(!this.isHover(e.getX(), e.getY()) || !use)
			return;
		
		for(ButtonEvent evt : observers)
			evt.onClicked();
	}
}
