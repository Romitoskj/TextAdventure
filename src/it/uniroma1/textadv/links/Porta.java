package it.uniroma1.textadv.links;

/**
 * Rappresenta una porta ossia un tipo di {@link Link}.
 */
public class Porta extends Link {

    /**
     * Crea una porta chiusa.
     *
     * @param nome    il nome della porta
     * @param stanza1 una delle due stanze che collega
     * @param stanza2 l'altra stanza
     */
    public Porta(String nome, String stanza1, String stanza2) {
        super(nome, stanza1, stanza2, true);
    }
}
