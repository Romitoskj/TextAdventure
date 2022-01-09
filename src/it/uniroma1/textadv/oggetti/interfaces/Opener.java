package it.uniroma1.textadv.oggetti.interfaces;

import it.uniroma1.textadv.interfaces.Lockable;

/**
 * Rappresenta un oggetto che può sbloccare e bloccare un {@link Lockable}.
 */
public abstract class Opener extends Oggetto {

    /**
     * Nome del lockable che sblocca.
     */
    private final String toOpen;

    /**
     * Crea un opener a partire dal suo nome e dal nome del lockable che sblocca.
     *
     * @param nome   nome dell'opener
     * @param toOpen nome del lockable che sblocca
     */
    public Opener(String nome, String toOpen) {
        super(nome);
        this.toOpen = toOpen;
    }

    /**
     * Ritorna il nome del lockable che l'opener sblocca.
     *
     * @return il nome del lockable
     */
    public String getToOpen() {
        return toOpen;
    }
}
