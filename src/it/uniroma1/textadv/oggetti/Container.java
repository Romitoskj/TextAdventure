package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.Lockable;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;

public abstract class Container extends Oggetto implements Lockable {

    private final String contentName;

    private Storable content;

    private boolean closed = true;

    private boolean locked;

    public Container(String nome, String contentName, boolean locked) {
        super(nome);
        this.contentName = contentName;
        this.locked = locked;
    }

    public void put(Storable content) {
        this.content = content;
    }

    public String getContentName() {
        return contentName;
    }

    public boolean isEmpty() {
        return content == null;
    }

    public Storable removeContent(String name) throws ItemNotPresentException {
        if (isEmpty() || !(contentName.equalsIgnoreCase(name))) throw new ItemNotPresentException();
        Storable res = null;
        if (isOpen()) {
            res = content;
            content = null;
        }
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
    public String toString() {
        return nome + (isOpen()? (content != null? " che contiene " + content : " che non contiene più nulla") : "");
    }
}
