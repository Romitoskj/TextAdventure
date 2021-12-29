package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Storable;

public class Gatto extends Personaggio implements Storable {

	public Gatto(String nome) {
		super(nome);

	}

	@Override
	public String parla() {
		return "MIAO!";
	}
}
