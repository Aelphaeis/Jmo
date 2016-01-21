package jmo.patterns.observer;

public interface Observer {
	void update(Observable o, Object ... arg);
}
