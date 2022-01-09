package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.oggetti.interfaces.Blocker;

/**
 * Rappresenta una vite che blocca qualcosa.
 */
public class Vite extends Blocker {

    /**
     * Crea una vite a partire dal nome e dal nome di cosa blocca.
     *
     * @param nome   nome della vite
     * @param blocca nome di ciò che blocca
     */
    public Vite(String nome, String blocca) {
        super(nome, blocca, true);
    }
}
