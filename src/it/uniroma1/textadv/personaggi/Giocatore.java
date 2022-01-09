package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.*;
import it.uniroma1.textadv.exceptions.*;
import it.uniroma1.textadv.interfaces.Item;
import it.uniroma1.textadv.interfaces.Observer;
import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.interfaces.Subject;
import it.uniroma1.textadv.oggetti.Tesoro;
import it.uniroma1.textadv.personaggi.exceptions.AlreadyCreatedPlayerException;
import it.uniroma1.textadv.personaggi.exceptions.PlayerNotInitializedException;
import it.uniroma1.textadv.personaggi.interfaces.Personaggio;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Rappresenta il giocatore ossia il personaggio che l'utente controlla attraverso dei comandi.
 */
public class Giocatore extends Personaggio implements Subject {

    /**
     * L'unica istanza del giocatore.
     */
    private static Giocatore instance;

    /**
     * La stanza in cui si trova il giocatore.
     */
    private Stanza posizione;

    /**
     * Lista di observer.
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Crea il giocatore a partire dal suo nome e dalla {@link Stanza} in cui il gioco parte.
     *
     * @param nome  nome del giocatore
     * @param start stanza di partenza
     */
    private Giocatore(String nome, Stanza start) {
        super(nome);
        posizione = start;
    }


    // SINGLEBUILDERTON PATTERN

    /**
     * Restituisce l'istanza del giocatore se è stato inizializzato.
     *
     * @return il giocatore
     * @throws PlayerNotInitializedException se il giocatore non è stato ancora creato
     */
    public static Giocatore getInstance() {
        if (instance == null)
            throw new PlayerNotInitializedException();
        return instance;
    }


    /**
     * Inizializza il giocatore con il suo nome e la stanza da cui parte. Senza questa effettuare questa operazione sarà
     * impossibile ottenere un'istanza del giocatore e richiamare un qualsiasi metodo su di essa.
     *
     * @param nome  nome del giocatore
     * @param start stanza da cui parte il giocatore
     * @throws AlreadyCreatedPlayerException se il giocatore è già stato inizializzato
     */
    public static void init(String nome, Stanza start) {
        if (instance != null) throw new AlreadyCreatedPlayerException();
        instance = new Giocatore(nome, start);
    }

    /**
     * Restituisce la stanza in cui il giocatore si trova.
     *
     * @return La stanza in cui il giocatore è
     */
    public Stanza getPosizione() {
        return posizione;
    }

    /**
     * Restituisce una rappresentazione testuale dell'inventario nella lingua richiesta.
     *
     * @param l la lingua di gioco
     * @return una stringa rappresentante l'inventario
     */
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


    /**
     * Fa muovere il giocatore attraverso il link fornito.
     *
     * @param link link in cui il giocatore prova a passare
     * @return {@code true} se il link era aperto e il giocatore si è spostato, {@code false} altrimenti
     */
    public boolean goThrough(Link link) {
        if (link.isOpen()) {
            Set<Stanza> to = link.getStanze();
            to.remove(posizione);
            posizione = to.iterator().next();
            return true;
        } else
            return false;
    }

    /**
     * Lascia un oggetto che ha nell'inventario nella stanza in cui si trova.
     *
     * @param toLeave nome dell'oggetto da lasciare
     * @throws ItemNotPresentException se non ha l'oggetto richiesto
     */
    public void lascia(String toLeave) throws ItemNotPresentException {
        Storable s = getInventoryItem(toLeave);
        if (s == null) throw new ItemNotPresentException();
        remove(toLeave);
        posizione.add(s);
    }

    /**
     * Restituisce un item presente nella stanza dato il nome.
     *
     * @param name nome dell'item
     * @return l'item cercato
     * @throws ItemNotPresentException se l'item cercato non è presente nella stanza
     */
    public Item searchItem(String name) throws ItemNotPresentException {
        return posizione.get(name);
    }

    /**
     * Restituisce il link presente nella stanza dato una determinata direzione.
     *
     * @param d direzione del link
     * @return il link cercato
     * @throws ItemNotPresentException se nella direzione fornita non c'è un link
     */
    public Link searchLink(Direzione d) throws ItemNotPresentException {
        return posizione.getLink(d);
    }

    /**
     * Restituisce il link presente nella stanza dato il suo nome.
     *
     * @param nome nome del link
     * @return il link cercato
     * @throws ItemNotPresentException se il link cercato non è presente nella stanza
     */
    public Link searchLink(String nome) throws ItemNotPresentException {
        return posizione.getLink(nome);
    }

    /**
     * Prende un oggetto dalla stanza e lo mette nell'inventario se possibile.
     *
     * @param nome il nome dell'oggetto da prendere
     * @return {@code true} se l'oggetto è stato preso, {@code false} altrimenti
     * @throws ItemNotPresentException se nella stanza non è presente l'oggetto cercato
     */
    public boolean takeFromRoom(String nome) throws ItemNotPresentException {
        Item item = searchItem(nome);
        if (!(item instanceof Storable)) return false;
        if (item instanceof Subject) {
            Subject subject = (Subject) item;
            subject.notifyObservers();
            if (subject.hasObservers()) return false;
            if (subject instanceof Tesoro) notifyObservers();
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
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public boolean hasObservers() {
        return observers.size() > 0;
    }
}
