package widget.equalizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import helper.ColorVariator;
import helper.FFT_forwarder;
import processing.core.PApplet;
import processing.core.PShape;
import widget.PWidget;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class Spectrum extends PWidget implements SongListener {
	protected FFT eq;
	protected AudioPlayer song;
	
	protected ColorVariator variator = new ColorVariator();
	private Color _color = Color.WHITE;
		
	protected float[] boostMul;
	
	public Spectrum(Point position, Dimension dim)
	{
		super(position, dim);
		final int BAND = 64;
		boostMul = new float[BAND];
		for(int n = 0; n < boostMul.length; ++n)
			boostMul[n] = polyBoost(n, .7f, 16, BAND);
	}
	
	public void draw()
	{
		if(eq == null || song == null) return;
		
//		applet.line(posX, posY, posX + width, posY);
				
		if(song.isPlaying()) {
			eq.forward(song.mix);
			_color = variator.variate();
			_color.brighter(); 
			_color.brighter(); 
			_color.brighter();		
		}
		
		applet.stroke(_color.getRGB());
		applet.strokeWeight(9);
		
		equalizeLog(applet, pos.y, dim.width, dim.height, 0, 5);
		
		//equalizeLinear(applet, posX, posY, width, height);
	}



	private void equalizeLinear(PApplet applet, float posX, float posY, float width, float height) 
	{
		float step = width/eq.avgSize();
		final int SCALE = 7;
		
		for(int band = 0; band < eq.specSize(); ++band)
		{
			float bandHeight = eq.getBand(band)*SCALE;
			
			if(bandHeight > height)
				bandHeight = applet.norm(bandHeight, 0, 5000)*height;
			
			applet.fill(255);
			
			//final float placeX = posX + step, placeY = applet.height - posY;
			final float placeX = posX + step, placeY = posY + height;
			
			applet.stroke(255);
			applet.strokeWeight(1);
			//applet.line(placeX, placeY, placeX, placeY - bandHeight);
			applet.line(placeX, placeY, placeX, placeY - bandHeight);
			
			step += width/eq.specSize();
			
			applet.stroke(0,128,128);
			if(band % (eq.specSize()/12) == 0 && band != 0)
				applet.line(placeX, placeY, placeX, placeY - height);
			
//			if(band == eq.specSize() - 250)
//				applet.line(placeX, placeY, placeX, 200);
			
		}
	}

	private void equalizeLog(PApplet applet, float posY, float width, float height,
			final int IGNORE_LOWER, final int IGNORE_UPPER) 
	{
		final int OFFSET = 10;
		float step = width/(eq.avgSize() - IGNORE_UPPER - IGNORE_LOWER);
		
		for(int band = IGNORE_LOWER; band < eq.avgSize() - IGNORE_UPPER; ++band)
		{
			float bandHeight = eq.getAvg(band) * boostMul[band];
			
			//bandHeight *= (4-.5)/64f * band + 1; // linear
			//bandHeight *= (9*2 - 1)/(2*4096f)*Math.pow(band, 2) + 0.65; // exponential
			//bandHeight = applet.norm(bandHeight, 0f, 500)*height;
						
//			System.out.println(String.format("Band %d: %.2f", band, bandHeight));
			
//			final float placeX = posX + step, placeY = posY + height;
			final float placeX = (band - IGNORE_LOWER)*step + OFFSET, placeY = posY + height;
			
			if(bandHeight > height)
				bandHeight = height;
			
			applet.line(placeX, placeY, placeX, placeY - bandHeight);
			
			//step += width/eq.specSize();
						
		}
	}
		
	protected float polyBoost(int idx, float mul_start, float mul_end, int avgSize) {
		return (float) ((mul_end - mul_start)/Math.pow(avgSize, 2)*Math.pow(idx, 2) + mul_start);
	}
	
	@Override
	public void update(AudioPlayer song) {
		System.out.println("Updating Spectrum");

		eq = new FFT(song.bufferSize(), song.sampleRate());
		eq.logAverages(128, 8);
		System.out.println(eq.avgSize());
		this.song = song;
	}
}

