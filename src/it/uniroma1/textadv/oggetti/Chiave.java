package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Opener;

/**
 * Rappresenta una chiave che apre qualcosa.
 */
public class Chiave extends Opener implements Storable {

    /**
     * Crea una chiave a partire dal nome e
     *
     * @param nome   nome della chiave
     * @param toOpen nome di cosa apre
     */
    public Chiave(String nome, String toOpen) {
        super(nome, toOpen);
    }
}
