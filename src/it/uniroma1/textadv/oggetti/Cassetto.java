package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.oggetti.interfaces.Container;

/**
 * Rappresenta un cassetto che può contenere qualcosa.
 */
public class Cassetto extends Container {

    /**
     * Crea un cassetto a partire dal suo nome e dal nome del contenuto.
     *
     * @param nome     nome del cassetto
     * @param contents nome del contenuto
     */
    public Cassetto(String nome, String contents) {
        super(nome, contents, false);
    }
}
