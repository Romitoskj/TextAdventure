package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.oggetti.Subject;
import it.uniroma1.textadv.textengine.Command;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.ArrayList;
import java.util.List;

public class Venditore extends Personaggio implements Observer {

	private final List<Subject> toSell = new ArrayList<>();

	private final String needed = "soldi";

	public Venditore(String nome, String... toSell) {
		super(nome);
		Subject item;
		for (String name : toSell) {
			item = (Subject) Mondo.getInstance().getOggetto(name);
			this.toSell.add(item);
			item.registraObserver(this);
		}
	}

	@Override
	public void update() {
		System.out.println("\n" + getNome() + ": " + (Command.getLanguage().equals(EnglishAndItalian.IT)?
				"'Hey! Prima dovresti pagare...'":
				"'Hey! You have to pay first..."));
	}
	
	@Override
	public void store(Storable toStore) {
		if (toStore.getNome().equalsIgnoreCase(needed)) {
			for (Subject item: toSell) {
				item.rimuoviObserver(this);
			}
		}
		super.store(toStore);
	}

	@Override
	public String parla(Language language) {
		if (language.equals(EnglishAndItalian.IT)) return "Benvenuto nella mia ferramenta!";
		else return "Welcome to my hardware store!";
	}

	@Override
	public String toString() {
		return "Venditore{" +
				"nome='" + nome + '\'' +
				", inventario=" + inventario +
				", toSell=" + toSell +
				", needed='" + needed + '\'' +
				'}';
	}
}
