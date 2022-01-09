package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.oggetti.interfaces.Container;

/**
 * Rappresenta una scrivania che può contenere un oggetto.
 */
public class Scrivania extends Container {

    /**
     * Crea una scrivania a partire dal nome e dal nome del contenuto.
     *
     * @param nome     nome della scrivania
     * @param contents nome del contenuto
     */
    public Scrivania(String nome, String contents) {
        super(nome, contents, false);
    }
}
