//package widget.equalizer;
//
//import java.util.ArrayList;
//
//import ddf.minim.AudioPlayer;
//import ddf.minim.analysis.FFT;
//
//interface Subject
//{
//	void register(FreqListener o);
//	void unregister(FreqListener o);
//	void notifyObserver();
//}
//
//public class Equalizer implements Subject {
//	private AudioPlayer song;
//	//private FFT fft;
//	private ArrayList<FreqListener> observers;
//	
//	public Equalizer(AudioPlayer song)
//	{
//		observers = new ArrayList<FreqListener>();
//		setNewSong(song);
//	}
//	
//	public void setNewSong(AudioPlayer song)
//	{
//		if(song == null) return;
//		this.song = song;
//		//fft = new FFT(song.bufferSize(), song.sampleRate());
//		notifyObserver();
//	}
//
//	public void init()
//	{
//		if(song == null) return;
//		//fft.forward(song.mix);		
//		//notifyObserver();
//	}
//	
//	@Override
//	public void register(FreqListener newObserver) {
//		observers.add(newObserver);
//	}
//
//	@Override
//	public void unregister(FreqListener deleteObserver) {
//		int observerIdx = observers.indexOf(deleteObserver);
//		System.out.println("Observer " + (observerIdx+1) + " deleted");
//		observers.remove(observerIdx);
//	}
//
//	@Override
//	public void notifyObserver() {
//		for(FreqListener observer : observers)
//			observer.update(song);
//	}
//}
