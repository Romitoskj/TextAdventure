package it.uniroma1.textadv.oggetti;


import it.uniroma1.textadv.Item;
import it.uniroma1.textadv.textengine.languages.Language;

public abstract class Oggetto implements Item {

	protected String nome;

	public Oggetto(String nome) {
		this.nome = nome;
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return getNome();
	}

	@Override
	public String getDescription(Language language) {
		return getNome();
	}
}
