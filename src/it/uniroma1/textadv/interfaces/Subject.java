package it.uniroma1.textadv.interfaces;


public interface Subject extends Item {

    void registraObserver(Observer o);

    void rimuoviObserver(Observer o);

    void notificaObservers();

    boolean hasObservers();
}
