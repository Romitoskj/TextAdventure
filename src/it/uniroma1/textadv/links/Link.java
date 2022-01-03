package it.uniroma1.textadv.links;

import it.uniroma1.textadv.Lockable;
import it.uniroma1.textadv.Item;
import it.uniroma1.textadv.oggetti.Opener;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.HashSet;
import java.util.Set;

public class Link implements Item, Lockable {

	private final String nome;
	private final Set<String> stanze = new HashSet<>();
	private boolean closed;
	private boolean locked;

	public Link(String nome, String stanza1, String stanza2) {
		this.nome = nome;
		stanze.add(stanza1);
		stanze.add(stanza2);
	}
	public Link(String nome, String stanza1, String stanza2, boolean closed) {
		this(nome, stanza1, stanza2);
		this.closed = closed;
	}

	public Set<String> getStanze() {
		return new HashSet<>(stanze);
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public void lock(Opener opener) {
		if (nome.equals(opener.getToOpen())) {
			locked = true;
			closed = true;
		}
	}

	@Override
	public void unlock(Opener opener) {
		if (nome.equals(opener.getToOpen())) {
			locked = false;
			open();
		}
	}

	@Override
	public boolean isUnlocked() {
		return !locked;
	}

	@Override
	public boolean isOpen() {
		return !closed;
	}

	@Override
	public void open() {
		if (isUnlocked()) {
			closed = false;
		}
	}

	@Override
	public String getDescription(Language language) {
		if (language.equals(EnglishAndItalian.IT)) return "Il passaggio " + nome + " è " + (isOpen()? "aperto" : "chiuso");
		else return "The passage " + nome + " is " + (isOpen()? "open" : "closed");
	}
}
