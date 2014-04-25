package widget.equalizer;

import java.awt.Dimension;
import java.awt.Point;

import processing.core.PApplet;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class SpectrumBox extends Spectrum implements SongListener {

	private int AMOUNT;
	
	public SpectrumBox(final int AMOUNT, Point pos, Dimension dim) {
		super(pos, dim);
		this.AMOUNT = AMOUNT;
		boostMul = new float[AMOUNT];
		for(int i = 0; i < boostMul.length; ++i)
		{
			boostMul[i] = polyBoost(i, .5f, 16, AMOUNT);
			System.out.println(boostMul[i]);
		}
		
	}
	
	public void draw()
	{	
		final int GAP = 25;
		final int IGNORE_LOWER = 1;
		int step = 0;
				
		if(eq != null && song.isPlaying())
			eq.forward(song.mix);
		
		for(int n = 0; n < AMOUNT; ++n)
		{
			float grayVal = 0;
			
			if(eq != null)
				grayVal = eq.getAvg(n+IGNORE_LOWER);
			
			//System.out.println(grayVal);
			
			//float normGrayVal = applet.norm(grayVal, 0, 100);
			Rectangle.fill(applet, grayVal*boostMul[n]*4);
			Rectangle.setScale(.7f + (grayVal/300 * boostMul[n]));
			applet.strokeWeight(1 + grayVal/20 * boostMul[n]);
			Rectangle.draw(applet, pos.x + step, pos.y, dim.width/AMOUNT - GAP, dim.height);

			
			step += applet.width/AMOUNT;
		}
		//System.out.println();
				
	}

	@Override
	public void update(AudioPlayer song) {
		System.out.println("Updating SpectrumBox");
		eq = new FFT(song.bufferSize(), song.sampleRate());
		//eq.logAverages(128, 2); // result in 8 bands
		eq.logAverages(32, 1); 
		System.out.println(eq.avgSize());
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
