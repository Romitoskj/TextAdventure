package it.uniroma1.textadv.exceptions;

public class WronglyFormattedFileException extends Exception {
    public WronglyFormattedFileException() {
        super("The file must have all the world section.");
    }
}
