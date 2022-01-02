package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.exceptions.ActionNotKnownException;

@FunctionalInterface
public interface ActionFactory {

    Action createAction(String name) throws ActionNotKnownException;
}
