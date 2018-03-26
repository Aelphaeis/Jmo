package jmo.patterns.observer;

public interface Observer<T extends ObservableArgs> {
	void update(Object caller, T args);
}
