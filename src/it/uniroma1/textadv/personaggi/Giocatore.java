package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Stanza;
import it.uniroma1.textadv.exceptions.AlreadyCreatedPlayerException;
import it.uniroma1.textadv.exceptions.NotInitializedPlayerException;

public class Giocatore extends Personaggio {

	private static Giocatore instance;

	private Stanza posizione;
	
	private Giocatore(String nome) {
		super(nome);
	}
	
	/*
	 * SINGLEBUILDERTON PATTERN
	 */
	public static Giocatore getInstance() {
		if (instance == null) 
			throw new NotInitializedPlayerException();
		return instance;
	}
	
	public static void init(String nome) throws AlreadyCreatedPlayerException {
		if (instance != null) throw new AlreadyCreatedPlayerException();
		instance = new Giocatore(nome);
	}
}
