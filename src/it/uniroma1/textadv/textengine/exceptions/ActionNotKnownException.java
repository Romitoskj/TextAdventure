package it.uniroma1.textadv.textengine.exceptions;

/**
 * Eccezione sollevata se si cerca un azione non riconosciuta dal motore testuale nella lingua utilizzata.
 */
public class ActionNotKnownException extends Exception {

    /**
     * Crea una {@link ActionNotKnownException}.
     */
    public ActionNotKnownException() {
        super("This action is not known by the selected language.");
    }
}
