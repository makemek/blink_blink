package textbox;

import processing.core.PApplet;
import processing.core.PShape;

public class TextboxTester extends PApplet {
	
	private TextBox textBox;
	private TextBox textBox2;
	private PShape shape;
	
	public void setup() {
		this.size(300, 300, PApplet.P2D);
		textBox = new TextBox(this, 150, 200);
		textBox.text = "Hello";
		textBox.setTextSize(60);
		
		textBox2 = new TextBox(this, 150, 260);
		textBox2.text = "Processing";
		textBox2.setTextSize(10);
	
		
		background(0);
		
		textBox.draw(20, 10);
		textBox2.draw(120, 50);
	}
	

}
