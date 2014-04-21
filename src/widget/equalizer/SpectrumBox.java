package widget.equalizer;

import processing.core.PApplet;
import widget.PWidget;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class SpectrumBox extends Spectrum implements PWidget, SongListener {

	private int AMOUNT;
	private FFT eq;
//	private float[] average;
	//public float avg;
	
//	private PShape[] box;
//	private Rectangle[] boxes;
		
	public SpectrumBox(final int AMOUNT) {
		this.AMOUNT = AMOUNT;
//		average = new float[AMOUNT];
		
//		box = new PShape[AMOUNT];
//		boxes = new Rectangle[AMOUNT];
	}
	
/*//	private void averageBand()
//	{
//		final float SCALE = 7f;
//				
//		final int IGNORE_BAND = 250; // ignore last of bands due to too low values.
//		final int UPPER_BOUND = eq.specSize() - IGNORE_BAND;
//		final int LOWER_BOUND = 0;
//		
//		final int BAND_LIMIT = UPPER_BOUND/AMOUNT;
//		
//		float total = 0;
//		for(int band = LOWER_BOUND, n = 0; band <= UPPER_BOUND; ++band)
//		{
//			float bandVal = eq.getBand(band)*SCALE;
//			//System.out.println("BAND: " + band + " " + bandVal);
//			total += bandVal;
//			
//			if(band % BAND_LIMIT == 0 && band != LOWER_BOUND)
//			{
//				//bandMean.add(total/BAND_LIMIT);
//				average[n] = total/BAND_LIMIT;
//				
//				n++;
//				total = 0;
//			}
//		}
//		//System.out.println();
//	}*/
	
//	public float[] getAverage()
//	{
//		return average;
//	}
	
	// TODO add method 'addArduino' in this class
	
	public void draw(PApplet applet, float posX, float posY, float width, float height)
	{	
		final int SCALE = 7;
		final int GAP = 25;
		int step = 0;
				
//		if(eq != null) {
//			//avg = eq.calcAvg(4000, 10000);
//			avg = eq.calcAvg(8500, 22000)*3.5f;
//		}
		
		if(eq != null && song.isPlaying())
			eq.forward(song.mix);
		
		for(int n = 0; n < AMOUNT; ++n)
		{
//			if(eq != null)
//				average[n] = eq.getAvg(n + 1)*SCALE;
	
//			System.out.println(average[n]);
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
		
//		System.out.println();
		
	}

	
	private void shapeInit(PApplet applet, float posX, float posY, float width, float height) {
		
//		final int GAP = 10;
//		float step = 0;
//		for(int n = 0; n < box.length; ++n)
//		{
//			box[n] = applet.createShape(PShape.RECT, posX + step, posY, width/AMOUNT - GAP, height);
//			box[n].setStroke(applet.color(255));
//			step += (float)applet.width/AMOUNT;
//		}
		

	}
	
	//public void setAmount(final int AMOUNT) {this.AMOUNT = AMOUNT;}
	
	@Override
	public void update(AudioPlayer song) {
		System.out.println("Updating SpectrumBox");
//		FFT_forwarder.getInstance().removeForwarder(eq);
		eq = new FFT(song.bufferSize(), song.sampleRate());
		eq.linAverages(AMOUNT + 6);
		//eq.linAverages(AMOUNT + 4);
//		eq.logAverages(22, 12);
		//System.out.println(eq.avgSize());
		this.song = song;
		
		
//		FFT_forwarder.getInstance().addForwarder(eq, song);
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
		//applet.ellipse(posX, posY, 2, 2);
	}
}
