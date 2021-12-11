package it.uniroma1.textadv.links;

import it.uniroma1.textadv.Named;
import it.uniroma1.textadv.oggetti.Opener;

public class Link implements Named {

	private final String nome;
	private final String stanza1;
	private final String stanza2;
	private boolean locked;

	public Link(String nome, String stanza1, String stanza2) {
		this.nome = nome;
		this.stanza1 = stanza1;
		this.stanza2 = stanza2;
	}

	public String getNome() {
		return nome;
	}

	public void lock() {
		locked = true;
	} // forse richiedere opener anche per chiudere

	public boolean unlock(Opener opener) {
		if (nome.equals(opener.getToOpen())) {
			locked = false;
		}
		return !locked;
	}

	@Override
	public String toString() {
		return "{" +
				(locked? "LOCKED" : "") +
				'}';
	}
}
