package widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import helper.ColorVariator;
import processing.core.PApplet;
import widget.equalizer.SongListener;
import ddf.minim.AudioPlayer;

public class WaveForm extends PWidget implements SongListener {
	
	public WaveForm(Point position, Dimension dim) {
		super(position, dim);
	}

	private AudioPlayer wave;
	private ColorVariator variator = new ColorVariator();
	
	@Override
	public void update(AudioPlayer song) {
		wave = song;
	}

	@Override
	public void draw() {
		
		if(wave == null) return;
		
		float height = (float)dim.getHeight() / 2;
		
		applet.strokeWeight(2);
		Color _color = variator.variate();
		_color.brighter();
		_color.brighter();
		applet.stroke(_color.getRGB());
		applet.translate(pos.x, pos.y);
		
		for (int i = 0; i < wave.bufferSize() - 1; i++) {
			float x1 = applet.map(i, 0, wave.bufferSize(), 0, (float) dim.getWidth());
			float x2 = applet.map(i + 1, 0, wave.bufferSize(), 0, (float) dim.getWidth());

			// applet.line( x1, 50 + wave.left.get(i)*50, x2, 50 +
			// wave.left.get(i+1)*50 );
			applet.line(x1, height + wave.right.get(i) * 25, x2, height + wave.right.get(i + 1) * 25);
		}
		applet.translate(-pos.x, -pos.y);
	}

}
