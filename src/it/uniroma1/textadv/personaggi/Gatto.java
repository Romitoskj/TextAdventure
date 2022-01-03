package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

public class Gatto extends Personaggio implements Animal, Storable {

	public Gatto(String nome) {
		super(nome);

	}

	@Override
	public String parla(Language language) {
		if (language.equals(EnglishAndItalian.IT)) return "MIAO!";
		else return "MEOW!";
	}
}
