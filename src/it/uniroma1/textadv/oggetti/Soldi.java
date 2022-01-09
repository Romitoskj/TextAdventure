package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Oggetto;

/**
 * Rappresenta dei soldi.
 */
public class Soldi extends Oggetto implements Storable {

    /**
     * Crea dei soldi a partire dal loro nome.
     *
     * @param nome il nome dei soldi
     */
    public Soldi(String nome) {
        super(nome);
    }
}
