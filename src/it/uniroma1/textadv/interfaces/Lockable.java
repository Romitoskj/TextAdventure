package it.uniroma1.textadv.interfaces;

import it.uniroma1.textadv.oggetti.interfaces.Opener;

/**
 * Rappresenta tutto ciò che può essere bloccato o chiuso nel mondo di gioco.
 */
public interface Lockable extends Item {

    /**
     * Blocca l'oggetto.
     *
     * @param opener che apre/chiude l'oggetto
     */
    void lock(Opener opener);

    /**
     * Sblocca l'oggetto.
     *
     * @param opener che apre/chiude l'oggetto
     */
    void unlock(Opener opener);

    /**
     * Indica se l'oggetto è sbloccato.
     *
     * @return {@code true} se è sbloccato, {@code false} altrimenti
     */
    boolean isUnlocked();

    /**
     * Apre l'oggetto.
     */
    void open();

    /**
     * Indica se l'oggetto è aperto.
     * Un oggetto può essere aperto solo se è sbloccato.
     *
     * @return {@code true} se è aperto, {@code false} altrimenti
     */
    boolean isOpen();
}
