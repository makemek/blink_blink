package widget.equalizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import button.symbol.SymbolResource;
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
	private PShape[] bar;
	
	public Spectrum(Point position, Dimension dim)
	{
		super(position, dim);
		final int BAND = 64;
		
		boostMul = new float[BAND];
		bar = new PShape[BAND];
		for(int n = 0; n < boostMul.length; ++n)
		{
			boostMul[n] = polyBoost(n, .7f, 16, BAND);
			
			bar[n] = SymbolResource.primitive(PApplet.LINE, 0, 1);
			bar[n].setStrokeWeight(8);
			bar[n].setStroke(Color.WHITE.getRGB());
		}
	}
	
	public void draw()
	{
		if(eq == null || song == null) return;
				
		if(song.isPlaying()) {
			eq.forward(song.mix);
			_color = variator.variate();
			_color.brighter(); 
			_color.brighter(); 
			_color.brighter();		
		}
		
		applet.stroke(_color.getRGB());
		applet.strokeWeight(9);
		
//		equalizeLogPShape();
		equalizeLog(pos.y, dim.width, dim.height, 0, 5);
		
		//equalizeLinear(posX, posY, width, height);
	}


	private void equalizeLogPShape()
	{
		float step = dim.width/eq.avgSize();
		final int OFFSET = 20;
		
		float placeY = pos.y + dim.height;
		
		for(int band = 0; band < eq.avgSize(); ++band)
		{
			float bandHeight = eq.getAvg(band) * boostMul[band];
			float placeX = band*step + OFFSET;

			applet.pushMatrix();
			applet.translate(placeX, placeY);
			applet.scale(-bandHeight);
			applet.shape(bar[band]);
			applet.popMatrix();
		}
		
	}

	private void equalizeLinear(float posX, float posY, float width, float height) 
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

	private void equalizeLog(float posY, float width, float height,
			final int IGNORE_LOWER, final int IGNORE_UPPER) 
	{
		final int OFFSET = 10;
		float step = width/(eq.avgSize() - IGNORE_UPPER - IGNORE_LOWER);
		float placeY = posY + height;
		
		for(int band = IGNORE_LOWER; band < eq.avgSize() - IGNORE_UPPER; ++band)
		{
			float bandHeight = eq.getAvg(band) * boostMul[band];
			final float placeX = (band - IGNORE_LOWER)*step + OFFSET;
			
			if(bandHeight > height)
				bandHeight = height;
			
			applet.line(placeX, placeY, placeX, placeY - bandHeight);						
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

