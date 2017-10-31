package jmo.patterns.observer;
import java.util.ArrayList;

public abstract class AbstractObservable implements Observable {
    private boolean changed = false;
    private ArrayList<Observer> obs;
    
    public AbstractObservable(){
    	obs = new ArrayList<Observer>();
    }
    
    @Override
    public void addObserver(Observer o) {
		if (o == null){
			throw new NullPointerException();
		}
		if (!obs.contains(o)) {
			obs.add(o);
		}
	}

	@Override
	public void deleteObserver(Observer o) {
	  obs.remove(o);
	}

	@Override
	public void notifyObservers() {
		notifyObservers(new Object[0]);
	}

	@Override
	public void notifyObservers(Object... args) {
	    Object[] arrLocal;
	 	synchronized (this) {
	 	    if (!changed){
                 return;
	 	    }
            arrLocal = obs.toArray();
            clearChanged();
         }

         for (int i = arrLocal.length-1; i>=0; i--){
             ((Observer)arrLocal[i]).update(this, args);		
         }
	}
	@Override
	public void deleteObservers() {
		obs = new ArrayList<Observer>();
	}

	@Override
	public boolean hasChanged() {
		return changed;
	}

	@Override
	public int countObservers() {
		return obs.size();
	}
	protected synchronized void setChanged() {
		changed = true;
    }

    protected synchronized void clearChanged() {
    	changed = false;
    }
}
