/**
 * This application is created by
 * @author Apipol Niyomsak
 * 
 * Created using Eclipse IDE with Proclipsing plug-in. 
 * Then, later import to processing IDE for export.
 */

package applet;

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

	public void setup() {

		size(800, 600, P3D);

		mPlayer = new MusicPlayer(this);

		spectrum = new Spectrum();
		analyzer = new SpectrumBox(8);
		waveform = new WaveForm();

		mPlayer.register(spectrum);
		mPlayer.register(analyzer);
		mPlayer.register(waveform);

		smooth();
		this.frameRate(30);
		background(0);
		
		System.out.println("READY!");
	}

	public void draw() {
		final int SPEC_HEIGHT = 300, ANAL_HEIGHT = height / 8;

		// no blur region
		this.fill(0);
		this.noStroke();
		this.rect(0, 0, width, SPEC_HEIGHT - 170);

		waveform.draw(this, 0, 145, width, 30);
		mPlayer.draw(this, 10, 10, 50, 50);
		spectrum.draw(this, 10, 200, width, SPEC_HEIGHT);
		analyzer.draw(this, 8, 200 + SPEC_HEIGHT + 15, width, ANAL_HEIGHT);

		// blur region
		this.fill(0, 80);
		this.noStroke();
		this.rect(0, SPEC_HEIGHT - 170, width, height);
		
	}
		
	public void stop() {
		System.out.println("EXIT");
		mPlayer.dispose();
		
	}
	
	public void mouseReleased() {
		MouseListener[] listeners = this.getMouseListeners();	
		for(MouseListener lst : listeners) {
			lst.mouseReleased(null);
		}	
	}
}
