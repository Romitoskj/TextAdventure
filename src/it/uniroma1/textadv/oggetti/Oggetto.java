package it.uniroma1.textadv.oggetti;


import it.uniroma1.textadv.Item;

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
}
