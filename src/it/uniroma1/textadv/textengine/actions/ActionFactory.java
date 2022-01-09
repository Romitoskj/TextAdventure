package it.uniroma1.textadv.textengine.actions;

import it.uniroma1.textadv.textengine.exceptions.ActionNotKnownException;

public interface ActionFactory {

    Action getAction(String name) throws ActionNotKnownException;

    Action[] values();
}
