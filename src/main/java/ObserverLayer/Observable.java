package ObserverLayer;

public interface Observable {
    void notifyObservers();
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
}
