package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.oggetti.interfaces.Container;

/**
 * Rappresenta un armadio che può contenere qualcosa.
 */
public class Armadio extends Container {

    /**
     * Crea un armadio a partire dal nome e dal nome del suo contenuto.
     *
     * @param nome      nome dell'armadio
     * @param contenuto nome del contenuto
     */
    public Armadio(String nome, String contenuto) {
        super(nome, contenuto, true);
    }
}
