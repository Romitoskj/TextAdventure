package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.*;
import it.uniroma1.textadv.exceptions.AlreadyCreatedPlayerException;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.exceptions.PlayerNotInitializedException;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Subject;

import java.util.Set;

public class Giocatore extends Personaggio {

    private static Giocatore instance;

    private Stanza posizione;

    private Giocatore(String nome, Stanza start) {
        super(nome);
        posizione = start;
    }

    /*
     * SINGLEBUILDERTON PATTERN
     */
    public static Giocatore getInstance() {
        if (instance == null)
            throw new PlayerNotInitializedException();
        return instance;
    }

    public static void init(String nome, Stanza start) throws AlreadyCreatedPlayerException {
        if (instance != null) throw new AlreadyCreatedPlayerException();
        instance = new Giocatore(nome, start);
    }

    public Stanza getPosizione() {
        return posizione;
    }

    public String getInventario() {
        return inventario.keySet().toString();
    }


    public boolean goThrough(Link link) {
        if (link.isOpen()) {
            Set<String> to = link.getStanze();
            to.remove(posizione.getNome());
            posizione = Mondo.getInstance().getStanza(to.iterator().next());
            return true;
        } else
            return false;
    }

    public void lascia(String toLeave) throws ItemNotPresentException {
        Storable s = getInventoryItem(toLeave);
        if (s == null) throw new ItemNotPresentException();
        remove(toLeave);
        posizione.add(s);
    }

    public Item searchItem(String name) throws ItemNotPresentException {
        return posizione.get(name);
    }

    public Link searchLink(Direzione d) throws ItemNotPresentException {
        return posizione.getLink(d);
    }

    public Link searchLink(String nome) throws ItemNotPresentException {
        return posizione.getLink(nome);
    }

    public boolean takeFromRoom(String nome) throws ItemNotPresentException {
        Item item = searchItem(nome);
        if (!(item instanceof Storable)) return false;
        if (item instanceof Subject) {
            Subject subject = (Subject) item;
            subject.notificaObservers();
            if (subject.hasObservers()) return false;
        }
        posizione.remove(nome);
        store((Storable) item);
        return true;
    }

    @Override
    public String toString() {
        return "Giocatore{" +
                "nome='" + nome + '\'' +
                ", posizione=" + posizione +
                ", inventario=" + inventario +
                '}';
    }
}
