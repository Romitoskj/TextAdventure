package it.uniroma1.textadv;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta le varie direzioni in cui può muoversi il giocatore.
 */
public enum Direzione {

	NORD("NORD","N"), SUD("SUD","S"), EST("EST","E"), OVEST("OVEST","O", "W");

	/**
	 * Possibili nomi con cui ci si può riferire alle varie direzioni.
	 */
	private final Set<String> names;

	/**
	 * Crea una direzione a partire dai suoi possibili nomi.
	 * @param names sequenza di nomi
	 */
	Direzione(String... names) {
		this.names = new HashSet<>(Arrays.asList(names));
	}

	/**
	 * Restituisce una direzione dato un suo nome.
	 * @param name nome della direzione
	 * @return direzione.
	 */
	public static Direzione get(String name) {
		for (Direzione d: values()) {
			if(d.names.stream().anyMatch(name::equalsIgnoreCase))
				return d;
		}
		return null;
	}
}
