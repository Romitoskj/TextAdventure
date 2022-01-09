package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Opener;
import it.uniroma1.textadv.interfaces.Subject;
import it.uniroma1.textadv.interfaces.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta delle tronchesi che possono aprire qualcosa.
 */
public class Tronchesi extends Opener implements Storable, Subject {

    /**
     * Observers delle tronchesi.
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Crea delle tronchesi a partire dal loro nome e dal nome di ciò che aprono.
     *
     * @param nome nome delle tronchesi
     * @param apre nome di ciò che aprono
     */
    public Tronchesi(String nome, String apre) {
        super(nome, apre);
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
}
