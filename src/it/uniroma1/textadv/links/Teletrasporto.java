package it.uniroma1.textadv.links;

/**
 * Rappresenta un teletrasporto ossia un tipo di {@link Link}.
 */
public class Teletrasporto extends Link {

    /**
     * Crea un teletrasporto chiuso.
     *
     * @param nome    il nome del teletrasporto
     * @param stanza1 una delle due stanze che collega
     * @param stanza2 l'altra stanza
     */
    public Teletrasporto(String nome, String stanza1, String stanza2) {
        super(nome, stanza1, stanza2);
    }

}
