package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.Lockable;

public abstract class Container extends Oggetto implements Lockable {

    private final String contents;

    private boolean closed = true;

    private boolean locked;

    public Container(String nome, String contents, boolean locked) {
        super(nome);
        this.contents = contents;
        this.locked = locked;
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
        return "{" +
                "nome='" + nome + '\'' +
                ", contents='" + contents + '\'' +
                ", " + (locked? "LOCKED" : "UNLOCKED") +
                '}';
    }
}
