package widget.equalizer;

import processing.core.PApplet;
import widget.PWidget;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class SpectrumBox extends Spectrum implements PWidget, SongListener {

	private int AMOUNT;
	
	public SpectrumBox(final int AMOUNT) {
		this.AMOUNT = AMOUNT;
	}
	
	public void draw(PApplet applet, float posX, float posY, float width, float height)
	{	
		final int SCALE = 7;
		final int GAP = 25;
		int step = 0;
				
		if(eq != null && song.isPlaying())
			eq.forward(song.mix);
		
		for(int n = 0; n < AMOUNT; ++n)
		{
			float grayVal = 0;
			final int IGNORE_LOWER = 1;
			
			if(eq != null)
				grayVal = eq.getAvg(n + IGNORE_LOWER)*SCALE*3;
			
			float normGrayVal = applet.norm(grayVal, 0, 100);
			Rectangle.fill(applet, grayVal);
			Rectangle.setScale(1 + normGrayVal/12);
			applet.strokeWeight(1 + normGrayVal*2);
			Rectangle.draw(applet, posX + step, posY, width/AMOUNT - GAP, height);

			
			step += applet.width/AMOUNT;
		}
				
	}

	@Override
	public void update(AudioPlayer song) {
		System.out.println("Updating SpectrumBox");
		eq = new FFT(song.bufferSize(), song.sampleRate());
		eq.linAverages(AMOUNT + 6);
		this.song = song;
		
	}

}

class Rectangle {
	
	static float scale = 1;
	
	public static void fill(PApplet applet, float grayVal)
	{
		applet.fill(grayVal);
	}
	
	public static void setScale(float s)
	{
		scale = s;
	}
	
	public static void draw(PApplet applet, float posX, float posY,
			float width, float height) {
		applet.stroke(255);
		posX = posX + width/2;
		posY = posY + height/2;
		applet.rectMode(applet.CENTER);
		
		applet.pushMatrix();
		applet.translate(posX, posY);
		applet.scale(scale);
		applet.translate(-posX, -posY);
		applet.rect(posX, posY , width, height);
		applet.popMatrix();
		
		applet.rectMode(applet.CORNER);
	}
}
