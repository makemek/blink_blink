package widget.equalizer;

import java.awt.Dimension;
import java.awt.Point;

import processing.core.PApplet;
import processing.core.PShape;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class SpectrumBox extends Spectrum implements SongListener {

	private int AMOUNT;
	
	public SpectrumBox(final int AMOUNT, Point pos, Dimension dim) {
		super(pos, dim);
		this.AMOUNT = AMOUNT;
		boostMul = new float[AMOUNT];
		for(int i = 0; i < boostMul.length; ++i)
			boostMul[i] = polyBoost(i, .5f, 16, AMOUNT);
		
		initShape();
	}
	
	private void initShape()
	{
		final int GAP = 25;
		
		for(int n = 0, step = 0; n < AMOUNT; ++n)
		{
			float width = dim.width/AMOUNT - GAP;
			float height = dim.height;
			PShape rect = applet.createShape(PApplet.RECT, -width/2, -height/2, width, height);
			rect.width = width;
			rect.height = height;

			shapes.add(rect);
			step += applet.width / AMOUNT;
		}
	}
	
	public void draw()
	{	
		final int IGNORE_LOWER = 1;
		int step = 0;
		
		if(eq != null && song.isPlaying())
			eq.forward(song.mix);
		
		int n = 0;
		for(PShape s : shapes)
		{
			float grayVal = 0;
			if (eq != null)
				grayVal = eq.getAvg(n + IGNORE_LOWER);
			
			s.setFill(applet.color(grayVal*boostMul[n]*4));
			s.setStrokeWeight(1 + grayVal/20 * boostMul[n]);
			s.setStroke(255);
			
			// scale
			applet.pushMatrix();
			applet.translate(pos.x + step + s.width/2, pos.y + s.height/2);
			applet.scale(.7f + (grayVal/300 * boostMul[n]));
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
