package arduino;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import processing.core.PApplet;
import widget.equalizer.SongListener;
import button.Switch;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class ArduinoSongController extends ArduinoController implements
		SongListener, Runnable {

	private FFT linFFT, logFFT;
	private AudioPlayer song;
	private Thread thread;
	
	private static int AVG_SPLIT = 14;
	
	private float boostMul[] = null;

	public ArduinoSongController(PApplet applet, Switch bt) {
		super(applet, bt);
		
		thread = new Thread(this, "Arduino Thread");
		thread.start();
		
	}

	public void enable() {
		super.enable();
		
		if(boostMul == null && pins != null) {
			System.out.println("getting boost");
			boostMul = new float[pins.length];
			for(int n = 0; n < pins.length; ++n)
				boostMul[n] = polyBoost(n);
		}
		
		synchronized(this) {
			this.notifyAll();
		}
	}
	
	public void disable() {
		thread.interrupt();
		
		// wait for another thread to acknowledge interrupt
		while(thread.isInterrupted())
			;
		super.disable();
	}
	
	private void toArduinoLinear() {
		if (arduino == null)
			return;
		
		final float SCALE = 5;

		final int IGNORE_LOWER = 2;
//		final int FREQ_BAND = (int) (fft.specSize() * fft.getBandWidth() / AVG_SPLIT);

		
		try 
		{
			for (int i = 0; i < pins.length; ++i) 
			{
				final int AVG_IDX = pins.length - i + IGNORE_LOWER - 1;
				float avg = PApplet.map(averageFreq(linFFT, AVG_IDX), 0, 10, 0, 4);
				int brightness = (int)(linFFT.getAvg(AVG_IDX) * SCALE * avg);
				
				if (i == 0)
					brightness <<= 1; // extra boost
					
				System.out.printf("PIN %2d %3d %.4f\n", pins[i], (int)brightness, avg);
				
				writeAnalogLED(pins[i], (int) brightness);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error Occurred!");
			this.disposeArduino();
		}
		 System.out.println();
	}
	
	private void toArduinoLog() {
		if (arduino == null)
			return;
		
		final float SCALE = 4;

//		final int IGNORE_LOWER = 0;
//		final int FREQ_BAND = (int) (logFFT.specSize() * logFFT.getBandWidth() / AVG_SPLIT);

		try 
		{
			float avg = PApplet.map(averageFreq(logFFT, 0), 0, 150, 0, 2);
			float brightness = logFFT.getAvg(0) * boostMul[0] * avg;
			System.out.printf("PIN %2d %3d %.4f\n", pins[pins.length - 1], (int)brightness, avg);
			writeAnalogLED(pins[pins.length - 1], (int)brightness);
			
			for (int i = 1; i < pins.length; ++i) 
			{
				// skip 2nd band (not much difference & interval too narrow & to cover more frequency)
				avg = PApplet.map(averageFreq(logFFT, i + 1), 0, 100, 0, 2.5f);
				brightness = logFFT.getAvg(i + 1) * boostMul[i] *avg * 1.35f;
				
//				if(i == pins.length - 1)
//					brightness *= 1.25; // extra boost
				
				System.out.printf("PIN %2d %3d %.4f\n", pins[pins.length - i - 1], (int)brightness, avg);
				writeAnalogLED(pins[pins.length - i - 1], (int)brightness);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error Occurred!");
			this.disposeArduino();
		}
		 System.out.println();
	}
	
	
	private void writeAnalogLED(int pin, int val)
	{
		if(val > 0xFF)
			val = 0xFF;
		arduino.analogWrite(pin, val);
	}
		
	private float averageFreq(FFT fft, int idx) {
		float centerFreq = fft.getAverageCenterFrequency(idx);
		float avgWidth = fft.getAverageBandWidth(idx);
		return fft.calcAvg(centerFreq - avgWidth/2, centerFreq + avgWidth/2);
	}
	
//	private float expoBoost(int idx) {
//		final float FIRST_MUL = .6f, LAST_MUL = 60;
//		double a = (LAST_MUL - FIRST_MUL)/(Math.exp(pins.length) - 1);
//		return (float) (a * Math.exp(idx) + FIRST_MUL - a);
//	}

	private float polyBoost(int idx) {
		final float FIRST_MUL = .5f, LAST_MUL = 65;
		return (float) ((LAST_MUL - FIRST_MUL)/Math.pow(pins.length, 2)*Math.pow(idx, 2) + FIRST_MUL);
	}
	
	@Override
	public void update(AudioPlayer song) {
		// TODO Find a way to get average up to number of pins		
		this.song = song;
		
//		FFT_forwarder.getInstance().removeForwarder(fft);
		
		if(pins != null)
			AVG_SPLIT = pins.length + 6;
		
		linFFT = new FFT(song.bufferSize(), song.sampleRate());
		linFFT.linAverages(AVG_SPLIT); // linear
		
//		fft.logAverages(3700, 2);
//		System.out.println("Sample Rate: " + song.sampleRate());
//		System.out.println("Result freq: " + song.sampleRate() / (1 << 6));
//		System.out.println("Rounding: " + Math.round(song.sampleRate() / (1 << 6)));
//		fft.logAverages(11024 / (1 << 6), 1);
		
		logFFT = new FFT(song.bufferSize(), song.sampleRate());
		logFFT.logAverages(((int)song.sampleRate() >> 2) / (1 << 6), 1); // logarithmic
		
//		fft.logAverages(11, 1);
//		System.out.println(fft.avgSize());
		
//		FFT_forwarder.getInstance().addForwarder(fft, song);
	}

	
	@Override
	public void run() {
		final int TxRate = 30;
		while (true) {
			try 
			{				
				if(thread.isInterrupted() || song == null || !song.isPlaying()) {
					synchronized(this) { 
							Thread.interrupted();
							System.out.println("WAITING");
							this.wait();
						}
				}
					
				else {
					logFFT.forward(song.mix);
					toArduinoLog();
//					toArduinoLinear();
				}
				
				Thread.sleep(TxRate);
				
			} catch (InterruptedException e /*Sleep interrupt*/) {
					System.err.println(e);
					thread.interrupt(); // raise flag again
				}
		}
	}
	
	
}
