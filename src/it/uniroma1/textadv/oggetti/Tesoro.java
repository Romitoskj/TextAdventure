package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.personaggi.Observer;

import java.util.ArrayList;
import java.util.List;

public class Tesoro extends Oggetto implements Storable, Subject {

    private final List<Observer> observers = new ArrayList<>();

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
    public void notificaObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    @Override
    public boolean hasObservers() {
        return observers.size() > 0;
    }
}
