package it.uniroma1.textadv;

import it.uniroma1.textadv.oggetti.Subject;
import it.uniroma1.textadv.personaggi.Giocatore;

public class Test {

	public static void main(String[] args) throws Exception {
		Gioco g = new Gioco();
		Mondo m = Mondo.fromFile("minizak.game");
		//System.out.println(m);
		//System.out.println(Giocatore.getInstance());
		System.out.println(m.getOggetti());
		System.out.println(m.getLinks());
		//System.out.println(m.getPersonaggi());

		Giocatore p = Giocatore.getInstance();
		System.out.println(p);
		p.goThrough(p.getPosizione().getLink("salone di Zak"));
		System.out.println(p);
		p.goThrough(p.getPosizione().getLink(Direzione.NORD));
		System.out.println(p);
		g.play(m);
	}
}