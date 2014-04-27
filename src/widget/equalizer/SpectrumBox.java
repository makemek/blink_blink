package widget.equalizer;

import helper.ColorVariator;
import helper.Pair;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PShape;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class SpectrumBox extends Spectrum implements SongListener {

	private int amount = 0;
	private ColorVariator variator = new ColorVariator();
	private boolean busy = false;
	
	private ArrayList<Pair<PShape, Float>> shapeBoost = new ArrayList<>();
	
	private final int IGNORE_LOWER = 1;
	private final int IGNORE_UPPER = 1;
	
	public SpectrumBox(Point pos, Dimension dim) {
		super(pos, dim);
		variator.setStep(.25f);
	}
	
	private PShape createShape()
	{
		final int GAP = 25;
		
		float width = dim.width/amount - GAP;
		float height = dim.height;
		PShape rect = applet.createShape(PApplet.ELLIPSE, -width/2, -height/2, width, height);
		rect.width = width;
		rect.height = height;
		
		rect.disableStyle();
		
		return rect;
		
	}
	
	public void draw()
	{	
		int step = 0;
		
		if(eq == null)
			return;
		
		if(song != null && song.isPlaying())
		{
			eq.forward(song.mix);
			variator.variate();
		}
		
		while(busy) {
			synchronized(this){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		int n = 0;
		for(Pair<PShape, Float> p : shapeBoost)
		{
			PShape s = p.getLeft();
			
			float grayVal = 0;
			grayVal = eq.getAvg(n + IGNORE_LOWER) * p.getRight();
			
			applet.fill(variator.getColor().getRGB(), grayVal);
			applet.stroke(variator.getColor().brighter().getRGB());
			
			applet.strokeWeight(2 + grayVal/30);
			
			// scale
			applet.pushMatrix();
			applet.translate(pos.x + step + s.width/2, pos.y + s.height/2);
			applet.scale(.5f + (grayVal/400));
			applet.shape(s);
			applet.popMatrix();

			step += applet.width / amount;
			++n;
		}
	}

	@Override
	public void update(AudioPlayer song) {
		System.out.println("Updating SpectrumBox");
		eq = new FFT(song.bufferSize(), song.sampleRate());
		//eq.logAverages(128, 2); // result in 8 bands
		eq.logAverages(32, 1); 
				
		if(eq.avgSize() != amount)
		{
			synchronized(this) {
				busy = true;
				amount = eq.avgSize();
				int size = amount - IGNORE_LOWER - IGNORE_UPPER;
								
				shapeBoost.clear();
				
				for(int n = 0; n < size; ++n)
				{
					float boost = polyBoost(n + IGNORE_LOWER, .3f, 16, size);
					PShape s = createShape();
					shapeBoost.add(new Pair<PShape, Float>(s, boost));
				}
				
				amount = size;
				busy = false;
				
				this.notify();
			}
		}
		
		System.out.println(amount);
		this.song = song;
		
	}

}
