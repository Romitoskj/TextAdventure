package it.uniroma1.textadv.exceptions;

/**
 * Eccezione sollevata se si cerca di ottenere l'istanza del {@link it.uniroma1.textadv.Mondo} quando non è ancora stata
 * creata.
 */
public class NotCreatedWorldException extends RuntimeException {

    /**
     * Crea una NotCreatedWorldException.
     */
    public NotCreatedWorldException() {
        super("Il mondo non è stato creato.");
    }
}
