package it.uniroma1.textadv.exceptions;

/**
 * Eccezione sollevata se si cerca di ottenere l'istanza del {@link it.uniroma1.textadv.personaggi.Giocatore} quando non
 * è ancora stata creata.
 */
@SuppressWarnings("serial")
public class NotInitializedPlayerException extends RuntimeException {

	/**
	 * Crea una NotInitializedPlayerException.
	 */
	public NotInitializedPlayerException() {
		super("You have to initialize the player first.");
	}
	
}
