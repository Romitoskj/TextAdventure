package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.oggetti.Subject;

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
		System.out.println("Hey! prima dovresti pagare...");
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
	public String parla() {
		return "Salve! Cosa posso fare per lei?";
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
