package it.uniroma1.textadv;

import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Container;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.personaggi.Personaggio;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.*;

/**
 * Rappresenta una stanza del mondo di gioco, ossia un qualsiasi spazio in cui può trovarsi il giocatore (sia aperto sia
 * chiuso). Una stanza può inoltre contenere oggetti e personaggi. Ogni stanza è collegata a una o più stanze attraverso
 * dei {@link Link}.
 */
public class Stanza implements Item {

    /**
     * Permette la costruzione e la creazione di una {@link Stanza}.
     */
    public static class Builder {
        // required parameters
        private final String nome;
        private final String descrizione;

        // optional parameters
        private final Map<String, Oggetto> oggetti = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        private final Map<String, Personaggio> personaggi = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        private final Map<Direzione, Link> links = new HashMap<>();

        /**
         * Crea un Builder dati il nome della stanza e la sua descrizione.
         *
         * @param nome        nome della stanza
         * @param descrizione descrizione della stanza
         */
        public Builder(String nome, String descrizione) {
            this.nome = nome;
            this.descrizione = descrizione;
        }

        /**
         * Aggiunge un oggetto alla stanza.
         *
         * @param oggetto {@link Oggetto} da aggiungere
         * @return Builder della stanza
         */
        public Builder addOggetto(Oggetto oggetto) {
            oggetti.put(oggetto.getName(), oggetto);
            return this;
        }

        /**
         * Aggiunge un personaggio alla stanza.
         *
         * @param personaggio {@link Personaggio} alla stanza
         * @return Builder della stanza
         */
        public Builder addPersonaggio(Personaggio personaggio) {
            personaggi.put(personaggio.getName(), personaggio);
            return this;
        }

        /**
         * Aggiunge un link a una determinata direzione della stanza.
         *
         * @param dir  {@link Direzione} in cui aggiungere il link
         * @param link {@link Link} da aggiungere
         * @return Builder della stanza
         */
        public Builder addLink(Direzione dir, Link link) {
            links.put(dir, link);
            return this;
        }

        /**
         * Crea la stanza.
         *
         * @return stanza
         */
        public Stanza build() {
            return new Stanza(this);
        }
    }

    /**
     * Nome della stanza.
     */
    private final String nome;

    /**
     * Descrizione della stanza.
     */
    private final String descrizione;

    /**
     * Dizionario degli oggetti presenti nella stanza indicizzati dal loro nome.
     */
    private final Map<String, Oggetto> oggetti;

    /**
     * Dizionario dei personaggi presenti nella stanza indicizzati dal loro nome.
     */
    private final Map<String, Personaggio> personaggi;

    /**
     * Dizionario dei link presenti nella stanza indicizzati dal loro nome.
     */
    private final Map<Direzione, Link> links;

    private Stanza(Builder builder) {
        nome = builder.nome;
        descrizione = builder.descrizione;
        oggetti = builder.oggetti;
        personaggi = builder.personaggi;
        links = builder.links;
    }

    @Override
    public String getName() {
        return nome;
    }

    @Override
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return nome + ": " + descrizione + "." +
                (oggetti.isEmpty() ? "" : "\nAll'interno ci sono degli oggetti: " + mapList(oggetti, language)) +
                (personaggi.isEmpty() ? "" : "\nC'è qualcuno qui dentro: " + mapList(personaggi, language)) +
                "\nPer uscire da questa stanza puoi sfruttare i seguenti passaggi: " + linkList(language);
        else return nome + ": " + descrizione + "." +
                (oggetti.isEmpty() ? "" : "\nInside the room there are some objects: " + mapList(oggetti, language)) +
                (personaggi.isEmpty() ? "" : "\nThere is someone in here: " + mapList(personaggi, language)) +
                "\nYou can use the following passages to exit from this room: " + linkList(language);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stanza)) return false;
        Stanza stanza = (Stanza) o;
        return getName().equals(stanza.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    private String mapList(Map<String, ? extends Item> m, Language language) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        Set<String> keySet = m.keySet();
        for (String s : keySet) {
            sb.append(m.get(s).getDescription(language))
                    .append((k == keySet.size() - 2) ? (language.equals(EnglishAndItalian.IT) ? " e " : " and ") : ", ");
            k++;
        }
        return sb.delete(sb.length() - 2, sb.length()).append(".").toString();
    }

    private String linkList(Language language) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        Set<Direzione> l = links.keySet();
        for (Direzione d : l) {
            sb.append((language.equals(EnglishAndItalian.IT) ? "a " : ""))
                    .append(d.get(language))
                    .append(" ")
                    .append(links.get(d).getName())
                    .append((k == l.size() - 2) ? (language.equals(EnglishAndItalian.IT) ? " e " : " and ") : ", ");
            k++;
        }
        return sb.delete(sb.length() - 2, sb.length()).append(".").toString();
    }

    /**
     * Restituisce il {@link Link} presente in una data {@link Direzione}.
     *
     * @param dir la direzione
     * @return il link richiesto
     * @throws ItemNotPresentException se in quella direzione non c'è nessun link
     */
    public Link getLink(Direzione dir) throws ItemNotPresentException {
        Link l = links.get(dir);
        if (l == null) throw new ItemNotPresentException();
        return l;
    }

    /**
     * Restituisce un {@link Link} dato il suo nome.
     *
     * @param nome il nome del link
     * @return il link richiesto
     * @throws ItemNotPresentException se non c'è nessun link con il nome fornito
     */
    public Link getLink(String nome) throws ItemNotPresentException {
        for (Link l : links.values()) {
            if (l.getName().equalsIgnoreCase(nome))
                return l;
        }
        throw new ItemNotPresentException();
    }

    /**
     * Aggiunge un oggetto alla stanza.
     *
     * @param s un oggetto di tipo {@link Storable}
     */
    public void add(Storable s) {
        if (s instanceof Oggetto) oggetti.put(s.getName(), (Oggetto) s);
        if (s instanceof Personaggio) personaggi.put(s.getName(), (Personaggio) s);
    }

    /**
     * Restituisce un {@link Item} presente nella stanza dato il suo nome.
     *
     * @param nome il nome dell'item
     * @return l'item richiesto
     * @throws ItemNotPresentException se non c'è nessun item con il nome fornito
     */
    public Item get(String nome) throws ItemNotPresentException {
        Item item = oggetti.get(nome.toLowerCase());
        if (item == null) {
            for (Oggetto o : oggetti.values()) {
                if (o instanceof Container) {
                    try {
                        item = ((Container) o).getContent(nome);
                    } catch (ItemNotPresentException ignored) {
                        // Si devono controllare gli altri container.
                    }
                }
            }
        }
        if (item == null) item = personaggi.get(nome.toLowerCase());
        if (item == null) item = getLink(nome);
        if (item == null) throw new ItemNotPresentException();
        return item;
    }

    /**
     * Rimuove un {@link Item} dalla stanza dato il suo nome (eccetto i {@link Link}).
     *
     * @param nome il nome dell'item
     */
    public void remove(String nome) {
        Item item = personaggi.remove(nome);
        if (item == null) oggetti.remove(nome);
        if (item == null) {
            for (Oggetto o : oggetti.values()) {
                if (o instanceof Container) {
                    try {
                        ((Container) o).takeContent(nome);
                    } catch (ItemNotPresentException ignored) {
                        // Si devono controllare gli altri container.
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Stanza{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", oggetti=" + oggetti +
                ", personaggi=" + personaggi +
                ", links=" + links +
                '}';
    }
}
