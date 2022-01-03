package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.personaggi.Observer;

import java.util.ArrayList;
import java.util.List;

public class Secchio extends Opener implements Storable, Subject {

    private boolean pieno = false;

    private final List<Observer> observers = new ArrayList<>();

    public Secchio(String nome) {
        super(nome, "camino");
    }

    public boolean isPieno() {
        return pieno;
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
        observers.forEach(Observer::update);
    }

    @Override
    public boolean hasObservers() {
        return observers.size() > 0;
    }

    public void riempi(Pozzo p) {
        pieno = true;
    }

    public void svuota() {
        pieno = false;
    }
}
