package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Opener;

/**
 * Rappresenta un cacciavite che può svitare una vite.
 */
public class Cacciavite extends Opener implements Storable {

    /**
     * Crea un cacciavite a partire dal suo nome.
     *
     * @param nome nome del cacciavite
     */
    public Cacciavite(String nome) {
        super(nome, "vite");
    }
}
