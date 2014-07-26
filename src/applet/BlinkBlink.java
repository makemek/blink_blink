/**
 * This application is created by
 * @author Apipol Niyomsak
 * 
 * Created using Eclipse IDE with Proclipsing plug-in. 
 * Then, later import to processing IDE for export.
 */

package applet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import button.symbol.Symbol;
import processing.core.PApplet;
import processing.core.PShape;
import widget.WaveForm;
import widget.equalizer.Spectrum;
import widget.equalizer.SpectrumBox;
import widget.musicplayer.MusicPlayer;

public class BlinkBlink extends PApplet {
	private SpectrumBox analyzer;
	private Spectrum spectrum;
	private MusicPlayer mPlayer;
	private WaveForm waveform;
	
	private final int BG_COLOR = Color.BLACK.getRGB();

	protected static BlinkBlink instance = null;
	
	private PShape blurBegin;
	private PShape blurEnd;
	
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
		analyzer = new SpectrumBox(new Point(8, 200 + SPEC_HEIGHT + 15), new Dimension(width, ANAL_HEIGHT));
		waveform = new WaveForm(new Point(0, 145), new Dimension(width, 30));

		mPlayer.register(spectrum);
		mPlayer.register(analyzer);
		mPlayer.register(waveform);

		blurBegin = Symbol.primitive(PApplet.RECT, width, (float) (spectrum.dim.getHeight() - 170));
		blurBegin.setStroke(false);
		blurBegin.setFill(BG_COLOR);
		blurEnd = Symbol.primitive(PApplet.RECT, width, height);
		blurEnd.setStroke(false);
		final int ALPHA = 80;
		blurEnd.setFill(BG_COLOR & (ALPHA << 24)); // set alpha to 80
		
		smooth();
		this.frameRate(30);
		
		System.out.println("READY!");
	}

	public void draw() {
		this.shape(blurBegin);
			
		waveform.draw();
		mPlayer.draw();
		spectrum.draw();
		analyzer.draw();
		
		this.shape(blurEnd, 0, (float) (spectrum.dim.getHeight() - 170));
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
