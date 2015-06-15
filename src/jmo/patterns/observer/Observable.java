package jmo.patterns.observer;

public interface Observable {
	public void addObserver(Observer o);
	public void deleteObserver(Observer o);
	public void notifyObservers();
	public void notifyObservers(Object ... args);
	public void deleteObservers();
	public boolean hasChanged();
	public int countObservers();
}
