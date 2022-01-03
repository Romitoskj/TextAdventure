package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

public class Cane extends Personaggio implements Animal {

	public Cane(String nome) {
		super(nome);

	}

	@Override
	public String parla(Language language) {
		if (language.equals(EnglishAndItalian.IT)) return "BAU!";
		else return "WOOF!";
	}

}
