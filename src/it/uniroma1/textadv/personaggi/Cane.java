package it.uniroma1.textadv.personaggi;

public class Cane extends Personaggio {

	public Cane(String nome) {
		super(nome);

	}

	@Override
	public String parla() {
		return "BAU!";
	}

}
