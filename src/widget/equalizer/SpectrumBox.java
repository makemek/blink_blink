package widget.equalizer;

import helper.ColorVariator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import processing.core.PApplet;
import processing.core.PShape;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class SpectrumBox extends Spectrum implements SongListener {

	private int AMOUNT;
	private ColorVariator variator = new ColorVariator();
	
	public SpectrumBox(final int AMOUNT, Point pos, Dimension dim) {
		super(pos, dim);
		this.AMOUNT = AMOUNT;
		boostMul = new float[AMOUNT];
		for(int i = 0; i < boostMul.length; ++i)
			boostMul[i] = polyBoost(i, .5f, 16, AMOUNT);
		
		variator.setStep(.25f);
		initShape();
	}
	
	private void initShape()
	{
		final int GAP = 25;
		
		for(int n = 0, step = 0; n < AMOUNT; ++n)
		{
			float width = dim.width/AMOUNT - GAP;
			float height = dim.height;
			PShape rect = applet.createShape(PApplet.ELLIPSE, -width/2, -height/2, width, height);
			rect.width = width;
			rect.height = height;
			
			rect.disableStyle();
			
			shapes.add(rect);
			step += applet.width / AMOUNT;
		}
	}
	
	public void draw()
	{	
		final int IGNORE_LOWER = 1;
		int step = 0;
		
		if(eq == null)
			return;
		
		if(song.isPlaying())
		{
			eq.forward(song.mix);
			variator.variate();
		}
		
		int n = 0;
		for(PShape s : shapes)
		{
			float grayVal = 0;
			grayVal = eq.getAvg(n + IGNORE_LOWER) * boostMul[n];
			
			applet.fill(variator.getColor().getRGB(), grayVal);
			applet.stroke(variator.getColor().brighter().getRGB());
			
			applet.strokeWeight(3 + grayVal/20);
			
			// scale
			applet.pushMatrix();
			applet.translate(pos.x + step + s.width/2, pos.y + s.height/2);
			applet.scale(.5f + (grayVal/300));
			applet.shape(s);
			applet.popMatrix();

			step += applet.width / AMOUNT;
			++n;
		}
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
