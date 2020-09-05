package jmo.patterns.observer;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

public class Observable<T extends Observable.Args> {

	private Queue<BiConsumer<Object, T>> obs = new ConcurrentLinkedQueue<>();

	public void addObserver(BiConsumer<Object, T> o) {
		if (!obs.contains(Objects.requireNonNull(o))) {
			obs.add(o);
		}
	}

	public void deleteObserver(BiConsumer<Object, T> o) {
		obs.remove(o);
	}

	public void notifyObservers(Object caller, T args) {
		obs.forEach(p -> p.accept(caller, Objects.requireNonNull(args)));
	}

	public void deleteObservers() {
		obs.clear();
	}

	public int countObservers() {
		return obs.size();
	}

	public static class Args {

		public static final Args EMPTY = new Args();

		protected Args() {}
	}
}