package it.uniroma1.textadv.links;

/**
 * Rappresenta un bus ossia un tipo di {@link Link}.
 */
public class Bus extends Link {

    /**
     * Crea un bus.
     *
     * @param nome    il nome della botola
     * @param stanza1 una delle due stanze che collega
     * @param stanza2 l'altra stanza
     */
    public Bus(String nome, String stanza1, String stanza2) {
        super(nome, stanza1, stanza2);
    }
}
