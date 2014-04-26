/**
 * This application is created by
 * @author Apipol Niyomsak
 * 
 * Created using Eclipse IDE with Proclipsing plug-in. 
 * Then, later import to processing IDE for export.
 */

package applet;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import processing.core.PApplet;
import widget.MusicPlayer;
import widget.WaveForm;
import widget.equalizer.Spectrum;
import widget.equalizer.SpectrumBox;

public class BlinkBlink extends PApplet {
	private SpectrumBox analyzer;
	private Spectrum spectrum;
	private MusicPlayer mPlayer;
	private WaveForm waveform;

	private static BlinkBlink instance = null;
	
	public static BlinkBlink getInstance()
	{
		if(instance == null)
			instance = new BlinkBlink();
		return instance;
	}
			
	public void setup() {
		instance = this;
		
		size(800, 600, P3D);

		final int SPEC_HEIGHT = 300, ANAL_HEIGHT = height >> 3;
		mPlayer = new MusicPlayer(new Point(10, 10), new Dimension(50, 50));
		spectrum = new Spectrum(new Point(10, 200), new Dimension(width, SPEC_HEIGHT));
		analyzer = new SpectrumBox(8, new Point(8, 200 + SPEC_HEIGHT + 15), new Dimension(width, ANAL_HEIGHT));
		waveform = new WaveForm(new Point(0, 145), new Dimension(width, 30));

		mPlayer.register(spectrum);
		mPlayer.register(analyzer);
		mPlayer.register(waveform);

		smooth();
		this.frameRate(30);
		background(0);
		
		System.out.println("READY!");
	}

	public void draw() {
		// no blur region
		this.fill(0);
		this.noStroke();
		this.rect(0, 0, width, (float) (spectrum.dim.getHeight() - 170));

		waveform.draw();
		mPlayer.draw();
		spectrum.draw();
		analyzer.draw();

		// blur region
		this.fill(0, 80);
		this.noStroke();
		this.rect(0, (float) (spectrum.dim.getHeight()) - 170, width, height);
		
	}
		
	public void stop() {
		System.out.println("EXIT");
		mPlayer.dispose();
	}
	
	public void mouseMoved(MouseEvent e)
	{
		super.mouseMoved(e);
		MouseListener[] listeners = this.getMouseListeners();	
		for(MouseListener lst : listeners) {
			lst.mouseEntered(e);
		}
		
	}
	

	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		MouseListener[] listeners = this.getMouseListeners();	
		for(MouseListener lst : listeners) {
			lst.mouseReleased(e);
		}	
	}
}
