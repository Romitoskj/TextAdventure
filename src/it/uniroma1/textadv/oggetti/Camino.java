package it.uniroma1.textadv.oggetti;

public class Camino extends Container {

    private boolean acceso = true;

    public Camino(String nome, String contents) {
        super(nome, contents);
    }

    public void spegni(Secchio s) {
        if (s.isPieno()) acceso = false;
    }
}
