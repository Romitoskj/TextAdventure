package it.uniroma1.textadv.exceptions;

import it.uniroma1.textadv.Mondo;

/**
 * Eccezione sollevata quando il file .game utilizzato per il caricamento del {@link Mondo} di gioco non è formattato
 * correttamente.
 */
public class MalformedGameFileException extends Exception {

    /**
     * Crea una {@link MalformedGameFileException}.
     *
     * @param msg messaggio che indica la causa
     */
    public MalformedGameFileException(String msg) {
        super(msg);
    }
}
