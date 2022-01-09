package it.uniroma1.textadv.oggetti.interfaces;

import it.uniroma1.textadv.interfaces.Lockable;
import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta un oggetto che può contenere altri oggetti.
 *
 * @see Storable
 */
public abstract class Container extends Oggetto implements Lockable {

    /**
     * Nome del contenuto.
     */
    private String contentName;

    /**
     * Contenuto.
     */
    private Storable content;

    /**
     * Indica se è chiuso o meno.
     */
    private boolean closed = true;

    /**
     * Indica se è bloccato o meno
     */
    private boolean locked;

    /**
     * Costruisce un contenitore a partire dal suo nome, dal nome del suo contenuto e da un flag che indica se è
     * bloccato o meno.
     *
     * @param nome        il nome del contenitore
     * @param contentName il nome del contenuto
     * @param locked      {@code true} se bloccato, {@code false} altrimenti
     */
    public Container(String nome, String contentName, boolean locked) {
        super(nome);
        this.contentName = contentName;
        this.locked = locked;
    }

    /**
     * Mette un oggetto all'interno del contenitore.
     *
     * @param content oggetto da mettere nel contenitore
     */
    public void put(Storable content) {
        this.content = content;
        contentName = content.getName();
    }

    /**
     * Restituisce il nome del contenuto.
     *
     * @return il nome del contenuto
     */
    public String getContentName() {
        return contentName;
    }

    /**
     * Indica se il contenitore è vuoto o meno.
     *
     * @return {@code true} se vuoto, {@code false} altrimenti
     */
    public boolean isEmpty() {
        return content == null;
    }

    /**
     * Restituisce il contenuto se il contenitore è aperto, se il nome fornito è uguale a quello del contenuto e se il
     * contenitore non è vuoto.
     *
     * @param name nome del contenuto che si cerca
     * @return il contenuto
     * @throws ItemNotPresentException se il contenitore è chiuso, vuoto oppure il contenuto cercato è diverso da quello
     *                                 dell'effettivo contenuto
     */
    public Storable getContent(String name) throws ItemNotPresentException {
        if (!isOpen() || isEmpty() || !(contentName.equalsIgnoreCase(name))) throw new ItemNotPresentException();
        return content;
    }

    /**
     * Rimuove il contenuto se il contenitore è aperto, se il nome fornito è uguale a quello del contenuto e se il
     * contenitore non è vuoto.
     *
     * @param name nome del contenuto che si cerca
     * @return il contenuto
     * @throws ItemNotPresentException se il contenitore è chiuso, vuoto oppure il contenuto cercato è diverso da quello
     *                                 dell'effettivo contenuto
     */
    public Storable takeContent(String name) throws ItemNotPresentException {
        if (!isOpen() || isEmpty() || !(contentName.equalsIgnoreCase(name))) throw new ItemNotPresentException();
        Storable res = content;
        content = null;
        return res;
    }

    @Override
    public void lock(Opener opener) {
        if (nome.equalsIgnoreCase(opener.getToOpen())) {
            locked = true;
            closed = true;
        }
    }

    @Override
    public void unlock(Opener opener) {
        if (nome.equalsIgnoreCase(opener.getToOpen())) {
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
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT))
            return nome + (isOpen() ? (content != null ? " che contiene " + content : " che non contiene più nulla") : "");
        else
            return nome + (isOpen() ? (content != null ? " that contains " + content : " that does not contain anything") : "");
    }
}
