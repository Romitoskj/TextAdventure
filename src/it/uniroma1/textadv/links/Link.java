package it.uniroma1.textadv.links;

import it.uniroma1.textadv.interfaces.Lockable;
import it.uniroma1.textadv.interfaces.Item;
import it.uniroma1.textadv.Stanza;
import it.uniroma1.textadv.links.exceptions.LinkAlreadyInitializedException;
import it.uniroma1.textadv.oggetti.interfaces.Opener;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta un collegamento tra due stanze.
 *
 * @see Stanza
 */
public class Link implements Item, Lockable {

    /**
     * Il nome del link.
     */
    private final String nome;

    /**
     * Il nome di una delle due stanze.
     */
    private final String STANZA1;

    /**
     * Il nome di una delle due stanze.
     */
    private final String STANZA2;

    /**
     * Set composto dalle due stanze collegate dal link (una volta che il link viene inizializzato).
     */
    private Set<Stanza> stanze;

    /**
     * Indica se il link è chiuso.
     */
    private boolean closed;

    /**
     * Indica se il link è chiuso da un {@link Opener}
     */
    private boolean locked;

    /**
     * Crea un link a partire dal suo nome e il nome delle due stanze che collega.
     *
     * @param nome    il nome del link
     * @param stanza1 il nome di una delle due stanze
     * @param stanza2 il nome dell'altra stanza
     */
    public Link(String nome, String stanza1, String stanza2) {
        this.nome = nome;
        STANZA1 = stanza1;
        STANZA2 = stanza2;
    }

    /**
     * Crea un link a partire dal suo nome, i nomi delle due stanze che collega e se è chiusa o meno.
     *
     * @param nome    il nome del link
     * @param stanza1 il nome di una delle due stanze
     * @param stanza2 il nome dell'altra stanza
     * @param closed  {@code true} se chiuso {@code false} altrimenti
     */
    public Link(String nome, String stanza1, String stanza2, boolean closed) {
        this(nome, stanza1, stanza2);
        this.closed = closed;
    }

    /**
     * Restituisce il nome della prima stanza.
     *
     * @return il nome della prima delle due stanze
     */
    public String getSTANZA1() {
        return STANZA1;
    }

    /**
     * Restituisce il nome della seconda stanza.
     *
     * @return il nome della seconda delle due stanze
     */
    public String getSTANZA2() {
        return STANZA2;
    }

    /**
     * Restituisce il {@link Set} delle stanze che il link collega.
     *
     * @return una copia del set delle stanze
     */
    public Set<Stanza> getStanze() {
        return new HashSet<>(stanze);
    }

    /**
     * Inizializza le stanze collegate dal link.
     *
     * @param s1 una delle due stanze
     * @param s2 l'altra stanza
     * @throws LinkAlreadyInitializedException se il link è già stato inizializzato
     */
    public void init(Stanza s1, Stanza s2) throws LinkAlreadyInitializedException {
        if (stanze != null) throw new LinkAlreadyInitializedException();
        stanze = Set.of(s1, s2);
    }

    @Override
    public String getName() {
        return nome;
    }

    @Override
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT))
            return "Il passaggio " + nome + " è " + (isOpen() ? "aperto" : "chiuso");
        else return "The passage " + nome + " is " + (isOpen() ? "open" : "closed");
    }

    @Override
    public void lock(Opener opener) {
        if (nome.equals(opener.getToOpen())) {
            locked = true;
            closed = true;
        }
    }

    @Override
    public void unlock(Opener opener) {
        if (nome.equals(opener.getToOpen())) {
            locked = false;
            open();
        }
    }

    @Override
    public boolean isUnlocked() {
        return !locked;
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public void open() {
        if (isUnlocked()) {
            closed = false;
        }
    }

    @Override
    public String toString() {
        return "Link{" + "nome='" + nome + '\'' + '}';
    }
}
