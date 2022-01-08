package it.uniroma1.textadv;

import it.uniroma1.textadv.oggetti.Opener;

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
     * @return true se è sbloccato, false altrimenti
     */
    boolean isUnlocked();

    /**
     * Apre l'oggetto.
     */
    void open();

    /**
     * Indica se l'oggetto è aperto.
     *
     * @return true se è aperto, false altrimenti
     */
    boolean isOpen();
}
