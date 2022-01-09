package it.uniroma1.textadv.links;

/**
 * Rappresenta una botola ossia un tipo di {@link Link}.
 */
public class Botola extends Link {

    /**
     * Crea una botola.
     *
     * @param nome    il nome della botola
     * @param stanza1 una delle due stanze che collega
     * @param stanza2 l'altra stanza
     */
    public Botola(String nome, String stanza1, String stanza2) {
        super(nome, stanza1, stanza2, true);
    }
}
