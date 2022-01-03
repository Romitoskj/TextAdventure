package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.Lockable;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

public abstract class Blocker extends Opener implements Lockable {

    private Link blocked;

    private boolean closed = true;

    private boolean locked;

    public Blocker(String nome, String blockedName, boolean locked) {
        super(nome, blockedName);
        this.locked = locked;
    }

    public void block(Link blocked) {
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
