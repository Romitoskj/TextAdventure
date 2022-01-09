package it.uniroma1.textadv.oggetti.interfaces;

import it.uniroma1.textadv.interfaces.Lockable;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta un oggetto che blocca un {@link Lockable}.
 */
public abstract class Blocker extends Opener implements Lockable {

    /**
     * Il {@link Lockable} che blocca.
     */
    private Lockable blocked;

    /**
     * Indica se è chiuso;
     */
    private boolean closed = true;

    /**
     * Indica se è bloccato.
     */
    private boolean locked;

    /**
     * Crea un blocker a partire dal suo nome, dal nome di ciò che blocca e se è bloccato o meno.
     *
     * @param nome il nome del blocker
     * @param blockedName il nome di ciò che blocca
     * @param locked {@code true} se bloccato, {@code false} altrimenti
     */
    public Blocker(String nome, String blockedName, boolean locked) {
        super(nome, blockedName);
        this.locked = locked;
    }

    /**
     * Blocca il {@link Lockable} fornito.
     *
     * @param blocked il lockable da bloccare
     */
    public void block(Lockable blocked) {
        this.blocked = blocked;
        blocked.lock(this);
    }

    private void release() {
        blocked.unlock(this);
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
    public void open() {
        if (isUnlocked()) {
            closed = false;
            release();
        }
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return nome + (isOpen()? "" : " che blocca " + getToOpen());
        else return nome + (isOpen()? "" : " that blocks " + getToOpen());
    }
}
