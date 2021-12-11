package it.uniroma1.textadv.oggetti;


import it.uniroma1.textadv.Named;

public abstract class Oggetto implements Named {

	protected String nome;

	public Oggetto(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return "{" +
				"nome='" + nome + '\'' +
				'}';
	}
}
