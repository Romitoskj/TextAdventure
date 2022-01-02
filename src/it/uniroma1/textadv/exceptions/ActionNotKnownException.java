package it.uniroma1.textadv.exceptions;

public class ActionNotKnownException extends Exception{

    public ActionNotKnownException() {
        super("This action is not known by the selected language.");
    }
}
