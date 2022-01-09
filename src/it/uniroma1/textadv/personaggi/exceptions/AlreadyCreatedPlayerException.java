package it.uniroma1.textadv.personaggi.exceptions;

/**
 * Eccezione sollevata se si prova a creare più di un giocatore.
 */
public class AlreadyCreatedPlayerException extends RuntimeException {

    /**
     * Crea una AlreadyCreatedPlayerException.
     */
    public AlreadyCreatedPlayerException() {
        super("The player has already been created and it must be the only one.");
    }

}
