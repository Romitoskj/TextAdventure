package it.uniroma1.textadv.oggetti;

public class Camino extends Container {

    public Camino(String nome, String contents) {
        super(nome, contents, true);
    }

    @Override
    public void unlock(Opener opener) {
        if (opener instanceof Secchio) {
            if (((Secchio) opener).isPieno()) super.unlock(opener);
        }
    }
}
