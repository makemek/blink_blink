package widget.musicplayer;

import helper.Drawable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import processing.core.PApplet;
import textbox.TextBox;
import widget.PWidget;
import widget.Publisher;
import widget.equalizer.SongListener;
import arduino.ArduinoSongController;
import button.AudioBrowser;
import button.Switch;
import button.primitive.Button;
import button.primitive.ButtonEvent;
import button.primitive.CircleButton;
import button.primitive.RectButton;
import button.symbol.Symbol;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class MusicPlayer extends PWidget implements Publisher<SongListener> {
	private AudioPlayer song;
	
	private Button playPauseBt;
	private Button stopBt;
	private Switch muteSwitch;
	private Switch loopSwitch;
	
	private AudioBrowser browser;
	
	private ArduinoSongController controller;
	public SongController songController;
	
	private TextBox track, timeStamp;
	
	private Collection<Drawable> buttons = new LinkedList<Drawable>();
	
	private ArrayList<SongListener> observers = new ArrayList<SongListener>();
	
	// TODO remove PApplet from TextBox's constructor
	
	public MusicPlayer(Point pos, Dimension dim) {
		super(pos, dim);
		
		browser = new AudioBrowser(new Minim(applet));
		layOutButton();
		this.register(controller);
		
		songController = new SongController(playPauseBt, stopBt, loopSwitch, muteSwitch);
		this.register(songController);
		
		timeStamp = new TextBox(applet, 100, 50);
		timeStamp.setStrokeColor(Color.ORANGE);
		
		track = new TextBox(applet, applet.width + 5, 35);
		track.setTextColor(Color.WHITE);
		track.setStrokeColor(Color.LIGHT_GRAY);
		track.setTextSize(25);
		
	}

	@Override
	public void draw() {

		applet.pushMatrix();
		applet.translate(pos.x, pos.y);
		applet.popMatrix();
		
		for(Drawable elem : buttons)
			elem.draw();
		
		track.draw(-5, 0);
		
		countDown();
		timeStamp.draw(applet.width - 120, 80 - 20);
	}

	private void countDown() {
		if(song == null) return;
		int timeLeft = (song.length() - song.position())/1000;
		
		timeStamp.text = String.format("%02d : %02d", timeLeft/60, timeLeft % 60);
		
		//System.out.println(song.length() - song.position());
		
		if (!song.isPlaying() && playPauseBt.getSymbol() instanceof PauseSymbol) {
			System.out.println("END");
			songController.stop();
			
			if(songController.isLooping())
				songController.play();
		}
	}
	
	private void browseMedia() {
		AudioPlayer newSong = browser.browse();
		
		if(newSong == null)
			return;
		
		song = newSong;
		
		notifyObserver();
		track.text = browser.getSelectedFile().getName();
		
		songController.play();
		System.out.println(song.length());					
	}
	
	private void layOutButton() {
		final float YPOS = 80, RADIUS = 20;
		
		playPauseBt = new CircleButton(30f, YPOS, RADIUS);
		playPauseBt.setColor(Color.ORANGE);
		playPauseBt.setSymbol(new PlaySymbol());

		stopBt = new CircleButton(90f, YPOS, RADIUS);
		stopBt.setColor(Color.YELLOW);
		stopBt.setSymbol(new StopSymbol());
		
		Button controllerBt = new CircleButton(150f, YPOS, RADIUS);
		setupBoardController(applet, controllerBt);
				
		Button muteBt = new CircleButton(210f, YPOS, RADIUS);
		muteBt.use(false);
		muteSwitch = getMuteSwitch(muteBt);

		Button loopBt = new CircleButton(270f, YPOS, RADIUS);
		loopBt.use(false);
		loopSwitch = getLoopSwitch(loopBt);
		
		RectButton bt = new RectButton(330, YPOS - 25, 100, 50);
		setupBrowseBt(applet, bt);

		buttons.add(playPauseBt);
		buttons.add(stopBt);
		buttons.add(bt);
		buttons.add(controllerBt);
		buttons.add(muteBt);
		buttons.add(loopBt);
	}

	private Switch getLoopSwitch(Button loopBt) {
		final Switch sw = new Switch(loopBt, new Symbol() {
			
			private LoopSymbol symLoop = new LoopSymbol();
			
			@Override
			public void draw(PApplet applet, float posX, float posY) {
				symLoop.draw(applet, posX, posY);
			}

		}, new Symbol() {
			
			private LoopSymbol symLoop = new LoopSymbol();
			private CrossSymbol symCross = new CrossSymbol();
			
			@Override
			public void draw(PApplet applet, float posX, float posY) {
				symLoop.draw(applet, posX, posY);
				symCross.draw(applet, posX, posY);
			}
		});
	
		return sw;
	}

	private Switch getMuteSwitch(Button muteBt) {
		
		Switch sw = new Switch(muteBt, new Symbol() {
			
			private MuteSymbol symMute = new MuteSymbol();
			private CrossSymbol symCross = new CrossSymbol();
			
			@Override
			public void draw(PApplet applet, float posX, float posY) {
				symMute.draw(applet, posX, posY);
				symCross.draw(applet, posX, posY);
			}
			
		}, new Symbol() {
			
			private MuteSymbol symMute = new MuteSymbol();
			
			@Override
			public void draw(PApplet applet, float posX, float posY) {
				symMute.draw(applet, posX, posY);
			}
		});
	
		return sw;
	}

	private void setupBoardController(final PApplet applet, Button controllerBt) {
		Switch controllerSwitch = new Switch(controllerBt, new Symbol() {
			@Override
			public void draw(PApplet applet, float posX, float posY) {
				applet.strokeWeight(0);
				applet.fill(Color.GREEN.getRGB());
				applet.ellipseMode(applet.CENTER);
				applet.ellipse(posX + width/2, posY + height/2, width, height);
			}
			
		}, new Symbol() {

			@Override
			public void draw(PApplet applet, float posX, float posY) {
				applet.strokeWeight(0);
				applet.fill(Color.RED.getRGB());
				applet.ellipseMode(applet.CENTER);
				applet.ellipse(posX + width/2, posY + height/2, width, height);	
				
			}
			
		});

		controller = new ArduinoSongController(applet, controllerSwitch);
	}

	private void setupBrowseBt(final PApplet applet, final RectButton bt) {
		bt.setSymbol(new Symbol() {
			private TextBox msg = new TextBox(applet, bt.getWidth(), bt.getHeight());
			@Override
			public void draw(PApplet applet, float posX, float posY) {
				msg.text = "BROWSE";
				msg.setTextSize(20);
//				msg.showBox(false);
				msg.setTextColor(Color.BLACK);
				msg.draw(posX, posY);
			}
			
		});
		bt.setColor(Color.ORANGE);
		
		bt.register(new ButtonEvent() {

			@Override
			public void onClicked() {
				Thread browseHandle = new Thread(new Runnable() {
					public void run() {
						browseMedia();
					}
					
				}, "Browse Handle Thread");
				browseHandle.start();
			}
			
		});
		
	}
	
	public void dispose() {
		songController.stop();
		if(song != null)
			song.close();
		
		controller.disable();
		controller.disposeArduino();
		
	}

	@Override
	public void register(SongListener newObserver) {
		observers.add(newObserver);

	}

	@Override
	public void unregister(SongListener deleteObserver) {
		int observerIdx = observers.indexOf(deleteObserver);
		System.out.println("Observer " + (observerIdx + 1) + " deleted");
		observers.remove(observerIdx);

	}

	@Override
	public void notifyObserver() {
		System.out.println("Notify observers");
		for (SongListener observer : observers)
			observer.update(song);
	}


}


class PauseSymbol implements Symbol {
	@Override
	public void draw(PApplet applet, float posX, float posY) {
		final float GAP = width / 4;
		final float RECT_WIDTH = (width - GAP) / 2;
		final int COLOR = Color.white.getRGB();

		applet.stroke(0);
		applet.strokeWeight(1);

		applet.fill(COLOR);
		applet.rect(posX, posY, RECT_WIDTH, height);
		applet.rect(posX + RECT_WIDTH + GAP, posY, RECT_WIDTH, height);

	}
}

class MuteSymbol implements Symbol {

	public void draw(PApplet applet, float posX, float posY) {
		applet.fill(0);
		applet.noStroke();
		
		applet.beginShape();
		applet.vertex(posX, posY + 25);
		applet.vertex(posX + this.width, posY);
		applet.vertex(posX + this.width, posY + this.height);
		applet.endShape();
		
		applet.rect(posX, posY, 20, this.height);	
	}
}

class CrossSymbol implements Symbol {
	public void draw(PApplet applet, float posX, float posY) {
		applet.stroke(0);
		applet.strokeWeight(4);
		applet.stroke(Color.RED.getRGB());
		applet.line(posX, posY + this.height,  posX + this.width, posY);
	}
}

class LoopSymbol implements Symbol {
	
	public void draw(PApplet applet, float posX, float posY) {
		applet.noFill();
		applet.stroke(0);
		applet.strokeWeight(4);
		
		byte half_width = width >> 1, half_height = height >> 1;
		
		applet.arc(posX + half_width, posY + half_height, half_width, half_height, -PApplet.HALF_PI, PApplet.PI);
	}
}

class PlaySymbol implements Symbol {
	@Override
	public void draw(PApplet applet, float posX, float posY) {

		final int OFFSET_X = 5;
		applet.fill(0);

		applet.beginShape();
		applet.vertex(OFFSET_X + posX, posY);
		applet.vertex(OFFSET_X + posX, posY + height);
		applet.vertex(OFFSET_X + posX + width, posY + height / 2);
		applet.endShape(applet.CLOSE);
	}
}

class StopSymbol implements Symbol {
	@Override
	public void draw(PApplet applet, float posX, float posY) {
		applet.rectMode(applet.CORNER);
		applet.fill(Color.RED.getRGB());
		applet.stroke(Color.ORANGE.getRGB());
		applet.strokeWeight(2);
		applet.strokeCap(applet.ROUND);
		applet.rect(posX, posY, width, height);
	}
}