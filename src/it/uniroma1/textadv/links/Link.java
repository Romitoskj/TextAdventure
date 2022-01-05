package it.uniroma1.textadv.links;

import it.uniroma1.textadv.Lockable;
import it.uniroma1.textadv.Item;
import it.uniroma1.textadv.Stanza;
import it.uniroma1.textadv.exceptions.LinkAlreadyInitializedException;
import it.uniroma1.textadv.oggetti.Opener;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.HashSet;
import java.util.Set;

public class Link implements Item, Lockable {

	private final String nome;
	private final String STANZA1;
	private final String STANZA2;
	private Set<Stanza> stanze;
	private boolean closed;
	private boolean locked;

	public Link(String nome, String stanza1, String stanza2) {
		this.nome = nome;
		STANZA1 = stanza1;
		STANZA2 = stanza2;
	}

	public String getSTANZA1() {
		return STANZA1;
	}

	public String getSTANZA2() {
		return STANZA2;
	}

	public Link(String nome, String stanza1, String stanza2, boolean closed) {
		this(nome, stanza1, stanza2);
		this.closed = closed;
	}

	public Set<Stanza> getStanze() {
		return new HashSet<>(stanze);
	}

	public void init(Stanza s1, Stanza s2) throws LinkAlreadyInitializedException {
		if (stanze != null) throw new LinkAlreadyInitializedException();
		stanze = Set.of(s1, s2);
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
