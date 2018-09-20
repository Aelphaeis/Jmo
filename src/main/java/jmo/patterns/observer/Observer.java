package jmo.patterns.observer;

@FunctionalInterface
public interface Observer<T extends ObservableArgs> {
	void update(Object caller, T args);
}
