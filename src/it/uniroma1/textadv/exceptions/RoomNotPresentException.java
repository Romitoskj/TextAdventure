package it.uniroma1.textadv.exceptions;

public class RoomNotPresentException extends Exception{
    public RoomNotPresentException() {
        super("Link must connect two existing rooms.");
    }
}
