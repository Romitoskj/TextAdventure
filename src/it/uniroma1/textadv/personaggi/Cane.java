package it.uniroma1.textadv.personaggi;

public class Cane extends Personaggio implements Animal {

	public Cane(String nome) {
		super(nome);

	}

	@Override
	public String parla() {
		return "BAU!";
	}

}
