package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.*;
import it.uniroma1.textadv.exceptions.*;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Subject;
import it.uniroma1.textadv.oggetti.Tesoro;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Giocatore extends Personaggio implements Subject{

    private static Giocatore instance;

    private Stanza posizione;

    private final List<Observer> observers = new ArrayList<>();

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

    public static void init(String nome, Stanza start) {
        if (instance != null) throw new AlreadyCreatedPlayerException();
        if (nome == null || nome.equals("")) throw new UnnamedPlayerException();
        if (start == null) throw new StartNotPresentException();
        instance = new Giocatore(nome, start);
    }

    public Stanza getPosizione() {
        return posizione;
    }

    public String getInventario(Language l) {
        StringBuilder sb = new StringBuilder();
        for (Storable s :
                inventario.values()) {
            sb.append("\t- ")
                    .append(s.getDescription(l))
                    .append("\n");
        }
        return sb.toString();
    }


    public boolean goThrough(Link link) {
        if (link.isOpen()) {
            Set<Stanza> to = link.getStanze();
            to.remove(posizione);
            posizione = to.iterator().next();
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
            if (subject instanceof Tesoro) notificaObservers();
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

    @Override
    public void registraObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void rimuoviObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notificaObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public boolean hasObservers() {
        return observers.size() > 0;
    }
}
