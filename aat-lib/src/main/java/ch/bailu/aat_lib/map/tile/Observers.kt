package ch.bailu.aat_lib.map.tile;

import org.mapsforge.map.model.common.ObservableInterface;
import org.mapsforge.map.model.common.Observer;

import java.util.ArrayList;

public class Observers implements ObservableInterface {
    private final ArrayList<Observer> observers = new ArrayList<>(2);



    public synchronized void addObserver(Observer observer) {
        observers.add(observer);
    }
    public synchronized void removeObserver(Observer observer) {
        observers.remove(observer);
    }


    public synchronized void notifyChange() {
        for (Observer o: observers) o.onChange();
    }
}
