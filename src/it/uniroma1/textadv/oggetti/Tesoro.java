package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Oggetto;
import it.uniroma1.textadv.interfaces.Subject;
import it.uniroma1.textadv.interfaces.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un tesoro.
 */
public class Tesoro extends Oggetto implements Storable, Subject {

    /**
     * observers del tesoro
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Crea un tesoro a partire dal suo nome.
     *
     * @param nome nome del tesoro
     */
    public Tesoro(String nome) {
        super(nome);
    }

    @Override
    public void registraObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void rimuoviObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public boolean hasObservers() {
        return observers.size() > 0;
    }
}
