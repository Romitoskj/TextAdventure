package it.uniroma1.textadv;

import it.uniroma1.textadv.oggetti.Opener;

public interface Lockable extends Item {

    void lock(Opener opener);

    void unlock(Opener opener);

    boolean isUnlocked();

    void open();

    boolean isOpen();
}
