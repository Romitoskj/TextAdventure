package it.uniroma1.textadv.exceptions;

public class WrongParameterException extends Exception{
    public WrongParameterException() {
        super("A command parameter must be item that are present in the world or stop words.");
    }
}
