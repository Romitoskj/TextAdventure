package it.uniroma1.textadv.personaggi.exceptions;

/**
 * Eccezione sollevata se si cerca di ottenere l'istanza del {@link it.uniroma1.textadv.personaggi.Giocatore} quando non
 * è ancora stata creata.
 */
public class PlayerNotInitializedException extends RuntimeException {

    /**
     * Crea una NotInitializedPlayerException.
     */
    public PlayerNotInitializedException() {
        super("You have to initialize the player first.");
    }

}
