package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.Stanza;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.exceptions.AlreadyCreatedPlayerException;
import it.uniroma1.textadv.exceptions.NotInitializedPlayerException;
import it.uniroma1.textadv.links.Link;

import java.util.Set;

public class Giocatore extends Personaggio {

	private static Giocatore instance;

	private Stanza posizione;
	
	private Giocatore(String nome, Stanza start) {
		super(nome);
		posizione = start;
	}
	
	/*
	 * SINGLEBUILDERTON PATTERN
	 */
	public static Giocatore getInstance() {
		if (instance == null) 
			throw new NotInitializedPlayerException();
		return instance;
	}
	
	public static void init(String nome, Stanza start) throws AlreadyCreatedPlayerException {
		if (instance != null) throw new AlreadyCreatedPlayerException();
		instance = new Giocatore(nome, start);
	}

	public Stanza getPosizione() {
		return posizione;
	}

	public String getInventario() {
		return inventario.keySet().toString();
	}


	public boolean goThrough(Link link) {
		if (link.isOpen()) {
			Set<String> to = link.getStanze();
			to.remove(posizione.getNome());
			posizione = Mondo.getInstance().getStanza(to.iterator().next());
			return true;
		} else
			return false;
	}

	public boolean lascia(String toLeave) {
		Storable s = getItem(toLeave);
		if (s == null) return false;
		remove(toLeave);
		posizione.add(s);
		return true;
	}

	@Override
	public String toString() {
		return "Giocatore{" +
				"nome='" + nome + '\'' +
				", posizione=" + posizione +
				", inventario=" + inventario +
				'}';
	}
}
