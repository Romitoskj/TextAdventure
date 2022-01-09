package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Opener;

/**
 * Rappresenta un martello.
 */
public class Martello extends Opener implements Storable {

    /**
     * Crea un martello a partire dal suo nome.
     *
     * @param nome nome del martello
     */
    public Martello(String nome) {
        super(nome, "salvadanaio");
    }
}
