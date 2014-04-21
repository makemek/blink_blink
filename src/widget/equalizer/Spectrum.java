package widget.equalizer;

import java.awt.Color;

import helper.ColorVariator;
import helper.FFT_forwarder;
import processing.core.PApplet;
import widget.PWidget;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class Spectrum implements PWidget, SongListener {
	protected FFT eq;
	protected AudioPlayer song;
	
	protected ColorVariator variator = new ColorVariator();
	private Color _color = Color.WHITE;
		
	private float[] boostMul;
	
	
	public void draw(PApplet applet, float posX, float posY, float width, float height)
	{
		if(eq == null) return;
		
//		applet.line(posX, posY, posX + width, posY);
		
		final int IGNORE_LOWER = 0, IGNORE_UPPER = 5;
		
		final int OFFSET = 10;
		float step = width/(eq.avgSize() - IGNORE_UPPER - IGNORE_LOWER);
		
		if(song.isPlaying()) {
			eq.forward(song.mix);
			_color = variator.variate();
			_color.brighter(); 
			_color.brighter(); 
			_color.brighter();		
		}
		
		applet.stroke(_color.getRGB());
		applet.strokeWeight(9);
		
		/* logarithm equalizer */
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
		
		
		
//		/* linear equalizer */
//		for(int band = 0; band < eq.specSize(); ++band)
//		{
//			float bandHeight = eq.getBand(band)*SCALE;
//			
//			if(bandHeight > height)
//				bandHeight = applet.norm(bandHeight, 0, 5000)*height;
//			
//			applet.fill(255);
//			
//			//final float placeX = posX + step, placeY = applet.height - posY;
//			final float placeX = posX + step, placeY = posY + height;
//			
//			applet.stroke(255);
//			applet.strokeWeight(1);
//			//applet.line(placeX, placeY, placeX, placeY - bandHeight);
//			applet.line(placeX, placeY, placeX, placeY - bandHeight);
//			
//			step += width/eq.specSize();
//			
//			applet.stroke(0,128,128);
//			if(band % (eq.specSize()/12) == 0 && band != 0)
//				applet.line(placeX, placeY, placeX, placeY - height);
//			
////			if(band == eq.specSize() - 250)
////				applet.line(placeX, placeY, placeX, 200);
//			
//		}
	}
	
	private float polyBoost(int idx) {
		final float FIRST_MUL = .7f, LAST_MUL = 16;
		return (float) ((LAST_MUL - FIRST_MUL)/Math.pow(eq.avgSize(), 2)*Math.pow(idx, 2) + FIRST_MUL);
	}
	
	@Override
	public void update(AudioPlayer song) {
		System.out.println("Updating Spectrum");
		
//		FFT_forwarder.getInstance().removeForwarder(eq);
		
		eq = new FFT(song.bufferSize(), song.sampleRate());
		//eq.logAverages(42, 10);
		eq.logAverages(128, 8);
//		eq.logAverages((int)song.sampleRate() / (1 << 6), 1);
		System.out.println(eq.avgSize());
		this.song = song;
		
		
//		FFT_forwarder.getInstance().addForwarder(eq, song);
		
		
		if(boostMul == null) {
			boostMul = new float[64];
			for(int n = 0; n < boostMul.length; ++n)
				boostMul[n] = polyBoost(n);
		}
		
	}
}

