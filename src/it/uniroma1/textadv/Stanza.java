package it.uniroma1.textadv;

import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.personaggi.Personaggio;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta una stanza del mondo di gioco, ossia un qualsiasi spazio in cui può trovarsi il giocatore (sia aperto sia
 * chiuso). Una stanza può inoltre contenere oggetti e personaggi. Ogni stanza è collegata a una o più stanze.
 */
public class Stanza implements Named {

    /**
     * Permette la costruzione e la creazione di una {@link Stanza}.
     */
    public static class Builder {
        // required parameters
        private String nome;
        private String descrizione;

        // optional parameters
        private Map<String, Oggetto> oggetti;
        private Map<String, Personaggio> personaggi;
        private Map<Direzione, Link> links;

        /**
         * Crea un Builder dati il nome della stanza e la sua descrizione.
         * @param nome nome della stanza.
         * @param descrizione descrizione della stanza.
         */
        public Builder(String nome, String descrizione) {
            this.nome = nome;
            this.descrizione = descrizione;
            oggetti = new HashMap<>();
            personaggi = new HashMap<>();
            links = new HashMap<>();
        }

        /**
         * Aggiunge un oggetto alla stanza.
         * @param oggetto {@link Oggetto} da aggiungere.
         * @return Builder della stanza.
         */
        public Builder addOggetto(Oggetto oggetto) {
            oggetti.put(oggetto.getNome(), oggetto);
            return this;
        }

        /**
         * Aggiunge un personaggio alla stanza.
         * @param personaggio {@link Personaggio} alla stanza.
         * @return Builder della stanza.
         */
        public Builder addPersonaggio(Personaggio personaggio) {
            personaggi.put(personaggio.getNome(), personaggio);
            return this;
        }

        /**
         * Aggiunge un link a una determinata direzione della stanza.
         * @param dir {@link Direzione} in cui aggiungere il link.
         * @param link {@link Link} da aggiungere.
         * @return Builder della stanza.
         */
        public Builder addLink(Direzione dir, Link link) {
            links.put(dir, link);
            return this;
        }

        /**
         * Crea la stanza.
         * @return stanza.
         */
        public Stanza build() {
            return new Stanza(this);
        }
    }

    /**
     * Nome della stanza.
     */
    private String nome;

    /**
     * Descrizione della stanza.
     */
    private String descrizione;

    /**
     * Dizionario degli oggetti presenti nella stanza indicizzati dal loro nome.
     */
    private Map<String, Oggetto> oggetti;

    /**
     * Dizionario dei personaggi presenti nella stanza indicizzati dal loro nome.
     */
    private Map<String, Personaggio> personaggi;

    /**
     * Dizionario dei link presenti nella stanza indicizzati dal loro nome.
     */
    private Map<Direzione, Link> links;

    /**
     * Crea una stanza a partire da un suo {@link Builder}.
     * @param builder
     */
    private Stanza(Builder builder) {
        nome = builder.nome;
        descrizione = builder.descrizione;
        oggetti = builder.oggetti;
        personaggi = builder.personaggi;
        links = builder.links;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }
}
