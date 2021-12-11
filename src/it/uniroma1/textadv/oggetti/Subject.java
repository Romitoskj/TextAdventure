package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.personaggi.Observer;

public interface Subject {

    void registraObserver(Observer o);

    void rimuoviObserver(Observer o);

    void notificaObservers();
}
