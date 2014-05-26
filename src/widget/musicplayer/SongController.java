package widget.musicplayer;

import helper.Switchable;
import widget.equalizer.SongListener;
import button.Switch;
import button.primitive.Button;
import button.primitive.ButtonEvent;
import ddf.minim.AudioPlayer;

class SongController implements SongListener
{
	private AudioPlayer song;
	private Button playPauseBt;
	private Button stopBt;
	private Switch loopSwitch;
	private Switch muteSwitch;
	
	private boolean looping = false;
	
	public SongController(final Button playPauseBt, final Button stopBt, final Switch loopSwitch, final Switch muteSwitch) {
		playPauseBt.register(new ButtonEvent() {

			@Override
			public void onClicked() {
				if(song == null) return;
				
				if (isButtonPlay()) {
					play();
				}
				

				else if (isButtonPause()) {
					pause();
				}

//				else if (!song.isPlaying() && isButtonPause)
//					stop();
				
			}

		});
		
		stopBt.register(new ButtonEvent() {

			@Override
			public void onClicked() {
				stop();
			}

		});
		
		muteSwitch.addEventListener(new Switchable() {
			@Override
			public void enable() {
				song.mute();
				
			}
			@Override
			public void disable() {
				song.unmute();
			}
		});
		
		loopSwitch.addEventListener(new Switchable() {
			@Override
			public void enable() {
				looping = true;
			}
			
			@Override
			public void disable() {
				looping = false;
			}
		});
		
		this.stopBt = stopBt;
		this.playPauseBt = playPauseBt;
		this.muteSwitch = muteSwitch;
		this.loopSwitch = loopSwitch;
	}
		
	public boolean isLooping() {return looping;}
	
	private boolean isButtonPlay() {
		return playPauseBt.getSymbol() instanceof PlaySymbol;
	}
	
	private boolean isButtonPause() {
		return playPauseBt.getSymbol() instanceof PauseSymbol;
	}
	
	public void play() {
		if(song == null) return;
		System.out.println("Play");
		
//		synchronized(controller) {
//			if(controller.isEnable())
//				controller.notifyAll();
//		}
		
		song.play();

		playPauseBt.setSymbol(new PauseSymbol());
	}
	
	public void pause() {
		if(song == null) return;
		System.out.println("Pause");
		song.pause();
		playPauseBt.setSymbol(new PlaySymbol());
		
	}
	
	public void stop() {
		if(song == null) return;
		System.out.println("STOP");
		playPauseBt.setSymbol(new PlaySymbol());
		song.rewind();
		song.pause();
	}
	
	@Override
	public void update(AudioPlayer song) {
		if(this.song != null)
			if(this.song.isMuted())
				song.mute();
		this.song = song;
		loopSwitch.getButton().use(true);
		muteSwitch.getButton().use(true);
	}
}