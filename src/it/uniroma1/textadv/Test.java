package it.uniroma1.textadv;

import it.uniroma1.textadv.oggetti.Subject;

public class Test {

	public static void main(String[] args) throws Exception {
		Gioco g = new Gioco();
		Mondo m = Mondo.fromFile("minizak.game");
		//System.out.println(m);
		//System.out.println(Giocatore.getInstance());
		//System.out.println(m.getOggetti());
		//System.out.println(m.getPersonaggi());
		System.out.println(m.getPersonaggio("guardiano del tesoro"));
		System.out.println(m.getPersonaggio("Tom"));
		((Subject) m.getOggetto("tesoro")).notificaObservers();
		((Subject) m.getOggetto("secchio")).notificaObservers();
		((Subject) m.getOggetto("tronchesi")).notificaObservers();
		m.getPersonaggio("guardiano del tesoro").store(((Storable)m.getPersonaggio("neo")));
		((Subject) m.getOggetto("tesoro")).notificaObservers();
		//System.out.println(m.getStanze());
		g.play(m);
	}
}
// TODO martello rompe salvadanaio
// TODO cacciavite svita vite
// TODO secchio si riempie nel pozzo
// TODO tronchesi aprono armadio luccicante
// TODO secchio PIENO spegne camino
// TODO scrivania e cassetto si aprono e si guardano