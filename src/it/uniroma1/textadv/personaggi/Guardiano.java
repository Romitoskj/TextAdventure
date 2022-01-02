package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.oggetti.Subject;

public class Guardiano extends Personaggio implements Observer {

	private final Subject watched;

	private final String needed;

	public Guardiano(String nome, String... params) {
		super(nome);
		needed = params[1];
		watched = (Subject) Mondo.getInstance().getOggetto(params[0]);
		watched.registraObserver(this);
	}

	@Override
	public void update() {
		System.out.println("\n" + getNome() + ": 'Non osare toccare il tesoro o passerai dei guai molto grossi!'");
	}

	@Override
	public void store(Storable toStore) {
		if (toStore.getNome().equalsIgnoreCase(needed)) {
			watched.rimuoviObserver(this);
		}
		super.store(toStore);
	}

	@Override
	public String parla() {
		return "Non mi distrarrai dal mio compito tanto facilmente! Finché sono vivo il tesoro non verrà neanche toccato!";
	}

	@Override
	public String toString() {
		return "Guardiano{" +
				"watched=" + watched +
				", needed='" + needed + '\'' +
				", nome='" + nome + '\'' +
				", inventario=" + inventario +
				'}';
	}
}
