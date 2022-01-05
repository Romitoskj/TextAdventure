package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.oggetti.Subject;
import it.uniroma1.textadv.textengine.Command;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

public class Guardiano extends Personaggio implements Observer {

	private final Subject watched;

	private final String needed;

	private boolean hasNeeded;

	private static final String ITA_WORKING = "Non mi distrarrai dal mio incarico tanto facilmente! Finché sono vivo il tesoro non verrà neanche toccato!";

	private static final String EN_WORKING = "You won't distract me from my task so easily! The treasure won't be even touched while as long as I am alive!";

	private static final String ITA_NOT = "Ma che bel micino!";

	private static final String EN_NOT = "What a beautiful kitty!";

	public Guardiano(String nome, String... params) throws ItemNotPresentException {
		super(nome);
		needed = params[1];
		watched = (Subject) Mondo.getInstance().getItem(params[0]);
		watched.registraObserver(this);
	}

	@Override
	public void update() {
		System.out.println("\n" + getNome() + ": " + (Command.getLanguage().equals(EnglishAndItalian.IT)?
				"'Non osare toccare il tesoro o passerai dei guai molto grossi!'" :
				"'Don't you dare touch the treasure or you will get in big trouble!'"));
	}

	@Override
	public void store(Storable toStore) {
		if (toStore.getNome().equalsIgnoreCase(needed)) {
			watched.rimuoviObserver(this);
			hasNeeded = true;
		}
		super.store(toStore);
	}

	@Override
	public String parla(Language language) {
		if (hasNeeded) {
			if (language.equals(EnglishAndItalian.IT)) return ITA_NOT;
			else return EN_NOT;
		}
		if (language.equals(EnglishAndItalian.IT)) return ITA_WORKING;
		else return EN_WORKING;	}

}
