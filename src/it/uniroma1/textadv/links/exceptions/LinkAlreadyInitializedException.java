package it.uniroma1.textadv.links.exceptions;

/**
 * Eccezione sollevata se si prova a inizializzare una seconda volta un {@link it.uniroma1.textadv.links.Link}.
 */
public class LinkAlreadyInitializedException extends RuntimeException {

    /**
     * Crea una {@link LinkAlreadyInitializedException}.
     */
    public LinkAlreadyInitializedException() {
        super("Links must be initialized only one time.");
    }
}
