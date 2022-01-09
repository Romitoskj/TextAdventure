package it.uniroma1.textadv.exceptions;

/**
 * Eccezione sollevata se si cerca di ottenere l'istanza del {@link it.uniroma1.textadv.Mondo} quando non è ancora stata
 * creata.
 */
public class WorldNotCreatedException extends RuntimeException {

    /**
     * Crea una {@link WorldNotCreatedException}.
     */
    public WorldNotCreatedException() {
        super("Il mondo non è stato creato.");
    }
}
