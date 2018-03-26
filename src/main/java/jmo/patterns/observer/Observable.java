package jmo.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable <T extends ObservableArgs>{
	private List<Observer<T>> obs;

	public Observable() {
		obs = new ArrayList<>();
	}

	public void addObserver(Observer<T> o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (!obs.contains(o)) {
			obs.add(o);
		}
	}

	public void deleteObserver(Observer<T> o) {
		obs.remove(o);
	}

	public void notifyObservers(Object caller, T args) {
		List<Observer<T>> observers;
		synchronized (this) {
			observers = new ArrayList<>(obs); 
		}
		
		for(Observer<T> observer : observers) {
			observer.update(caller, args);
		}
	}

	public void deleteObservers() {
		obs = new ArrayList<>();
	}

	public int countObservers() {
		return obs.size();
	}
}
