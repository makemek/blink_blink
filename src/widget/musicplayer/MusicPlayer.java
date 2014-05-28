package widget.musicplayer;

import helper.Drawable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PShape;
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
	
	private Switch playPauseBt;
	private Button stopBt;
	private Switch muteSwitch;
	private Switch loopSwitch;
	
	private AudioBrowser browser;
	
	private ArduinoSongController controller;
	public SongController songController;
	
	private TextBox track, timeStamp;
	
	private Collection<Drawable> buttons = new LinkedList<Drawable>();
	
	private ArrayList<SongListener> observers = new ArrayList<SongListener>();
	
	public MusicPlayer(Point pos, Dimension dim) {
		super(pos, dim);
		
		browser = new AudioBrowser(new Minim(applet));
		layOutButton();
		this.register(controller);
		
		songController = new SongController(playPauseBt, stopBt, loopSwitch, muteSwitch);
		this.register(songController);
		
		timeStamp = new TextBox(100, 50);
		timeStamp.setStrokeColor(Color.ORANGE);
		
		track = new TextBox(applet.width + 5, 35);
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
		
		if (!song.isPlaying() && playPauseBt.isEnable() /* && playPauseBt.getSymbol() instanceof PauseSymbol*/) {
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
		final float YPOS = 70, DIAMETER = 40;
			
		Button playBt = new CircleButton(30f, YPOS, DIAMETER);
		playBt.setColor(Color.ORANGE);
		playBt.use(false);
		playPauseBt = new Switch(playBt, Symbol.pause(), Symbol.play());

		stopBt = new CircleButton(90f, YPOS, DIAMETER);
		stopBt.setColor(Color.ORANGE);
		stopBt.setSymbol(Symbol.primitive(PApplet.RECT));
		
		Button controllerBt = new CircleButton(150f, YPOS, DIAMETER);
		setupBoardController(applet, controllerBt);
				
		Button muteBt = new CircleButton(210f, YPOS, DIAMETER);
		muteBt.use(false);
		
		PShape muteSpeaker = Symbol.groupShape(Symbol.speaker(), Symbol.cross());
		muteSwitch = new Switch(muteBt, muteSpeaker, Symbol.speaker());
		
		Button loopBt = new CircleButton(270f, YPOS, DIAMETER);
		PShape notLoop = Symbol.groupShape(Symbol.loop(), Symbol.cross());
		loopSwitch = new Switch(loopBt, Symbol.loop(), notLoop);
		loopBt.use(false);
		
		
		RectButton bt = new RectButton(330, YPOS - DIAMETER/4, 100, 50);
		setupBrowseBt(bt);

		buttons.add(playPauseBt);
		buttons.add(stopBt);
		buttons.add(bt);
		buttons.add(controllerBt);
		buttons.add(muteBt);
		buttons.add(loopBt);
	}

	private void setupBoardController(final PApplet applet, Button controllerBt) {
		
		PShape on = Symbol.primitive(PApplet.ELLIPSE);
		PShape off = Symbol.primitive(PApplet.ELLIPSE);
		on.setStroke(false);
		off.setStroke(false);
		off.setFill(Color.RED.getRGB());
		on.setFill(Color.GREEN.getRGB());
		
		Switch sw = new Switch(controllerBt, on, off);
		controller = new ArduinoSongController(applet, sw);
	}

	private void setupBrowseBt(final RectButton bt) {
//		bt.setSymbol(new Symbol() {
//			private TextBox msg = new TextBox(bt.getWidth(), bt.getHeight());
//			@Override
//			public void draw(PApplet applet, float posX, float posY) {
//				msg.text = "BROWSE";
//				msg.setTextSize(20);
////				msg.showBox(false);
//				msg.setTextColor(Color.BLACK);
//				msg.draw(posX, posY);
//			}
//			
//		});
		
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


