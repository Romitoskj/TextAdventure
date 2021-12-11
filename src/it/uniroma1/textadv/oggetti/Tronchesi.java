package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.personaggi.Observer;

import java.util.ArrayList;
import java.util.List;

public class Tronchesi extends Oggetto implements Storable, Subject {

    private String apre;

    private final List<Observer> observers = new ArrayList<>();

    public Tronchesi(String nome, String apre) {
        super(nome);
        this.apre = apre;
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
}
