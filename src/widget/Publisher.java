package widget;


public interface Publisher<Listener>
{
	void register(Listener o);
	void unregister(Listener o);
	void notifyObserver();
}