package helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class FFT_forwarder {
	
	static private Map<FFT, AudioPlayer> forwarder = new ConcurrentHashMap<>();
	
	private static FFT_forwarder instance = null;
	
	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true) {
				for(FFT fft : forwarder.keySet())
					fft.forward(forwarder.get(fft).mix);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}	
	});
	
	private FFT_forwarder() {
		thread.start();
	}
	
	public static FFT_forwarder getInstance() {
		if(instance == null)
			instance = new FFT_forwarder();
		return instance;
	}
	
	
	public void addForwarder(final FFT fft, final AudioPlayer song) {
		forwarder.put(fft, song);
	}
	
	public void removeForwarder(FFT fft) {
		if(fft == null) return;
		System.out.println(forwarder.remove(fft));
	}
}
