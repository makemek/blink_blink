package widget.musicplayer;

import helper.Switchable;
import widget.equalizer.SongListener;
import button.Switch;
import button.primitive.Button;
import button.primitive.ButtonEvent;
import button.symbol.SymbolResource;
import ddf.minim.AudioPlayer;

class SongController implements SongListener
{
	private AudioPlayer song;
	private Switch playBt;
	private Button stopBt;
	private Switch loopSwitch;
	private Switch muteSwitch;
	
	private boolean looping = false;
	
	public SongController(final Switch playPauseBt, final Button stopBt, final Switch loopSwitch, final Switch muteSwitch) {
		playPauseBt.addEventListener(new Switchable() {
			
			@Override
			public void enable() {
				play();
				
			}
			
			@Override
			public void disable() {
				pause();
				
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
		this.playBt = playPauseBt;
		this.muteSwitch = muteSwitch;
		this.loopSwitch = loopSwitch;
	}
		
	public boolean isLooping() {return looping;}
	
	public void play() {
		if(song == null) return;
		System.out.println("Play");
		System.out.println(playBt.isEnable());
		
//		synchronized(controller) {
//			if(controller.isEnable())
//				controller.notifyAll();
//		}
		
		playBt.on();
		song.play();
	}
	
	public void pause() {
		if(song == null) return;
		System.out.println("Pause");
		playBt.off();
		song.pause();	
	}
	
	public void stop() {
		if(song == null) return;
		System.out.println("STOP");
		
		playBt.off();
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
		playBt.getButton().use(true);
	}
}