package it.uniroma1.textadv.oggetti.interfaces;


import it.uniroma1.textadv.interfaces.Item;
import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta un oggetto presente in un mondo di gioco.
 */
public abstract class Oggetto implements Item {

	/**
	 * Nome dell'oggetto.
	 */
	protected final String nome;

	/**
	 * Crea un oggetto a partire dal suo nome.
	 *
	 * @param nome nome dell'oggetto
	 */
	public Oggetto(String nome) {
		this.nome = nome;
	}

	@Override
	public String getName() {
		return nome;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getDescription(Language language) {
		return getName();
	}
}
