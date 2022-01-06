package it.uniroma1.textadv;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


import it.uniroma1.textadv.exceptions.*;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Blocker;
import it.uniroma1.textadv.oggetti.Container;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.oggetti.Opener;
import it.uniroma1.textadv.personaggi.Giocatore;
import it.uniroma1.textadv.personaggi.Personaggio;

/**
 * Rappresenta il mondo di gioco.
 */
public class Mondo {

    /**
     * Istanza del mondo.
     */
    private static Mondo INSTANCE;

    /**
     * Nome del mondo.
     */
    private final String name;

    /**
     * Descrizione del mondo.
     */
    private final String description;

    /**
     * Dizionario contenente tutti gli oggetti del mondo indicizzati con il loro nome.
     */
    private final Map<String, Oggetto> OGGETTI = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Dizionario contenente tutti i personaggi del mondo indicizzati con il loro nome.
     */
    private final Map<String, Personaggio> PERSONAGGI = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Dizionario contenente tutte le stanze del mondo indicizzate con il loro nome.
     */
    private final Map<String, Stanza> STANZE = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Dizionario contenente tutti i link del mondo indicizzati con il loro nome.
     */
    private final Map<String, Link> LINKS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Crea un mondo di gioco a partire dal nome, dalla descrizione e dal nome della stanza di partenza.
     *
     * @param name        Il nome del mondo.
     * @param description La descrizione del mondo.
     */
    private Mondo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Restituisce un {@link Item} presente nel mondo di gioco dato il nome.
     *
     * @return item richiesto.
     */
    public Item getItem(String name) {
        Item item = OGGETTI.get(name);
        if (item == null) item = PERSONAGGI.get(name);
        if (item == null) item = STANZE.get(name);
        if (item == null) item = LINKS.get(name);
        return item;
    }

    /**
     * Restituisce l'istanza del mondo
     *
     * @return istanza del mondo
     */
    public static Mondo getInstance() {
        if (INSTANCE == null) throw new WorldNotCreatedException();
        return INSTANCE;
    }

    @Override
    public String toString() {
        return name + "\n" + description;
    }

    /**
     * Crea un mondo a partire da un file .game.
     *
     * @param percorso - stringa del percorso del file
     * @return un mondo di gioco
     * @throws IOException - se occorre un errore I/O leggendo il file
     * @throws WronglyFormattedFileException se il file .game non è formattato correttamente
     * @throws RoomNotPresentException se un link prova a collegare una stanza non presente
     * @throws RoomWithoutDescriptionException se non viene fornita la descrizione di una stanza
     * @throws ClassNotFoundException se non viene trovata la classe corrispondente a un item
     * @throws InvocationTargetException se la costruzione di un item solleva un eccezione
     * @throws NoSuchMethodException se il costruttore di un item non viene trovato
     * @throws InstantiationException se un item non può essere creato
     * @throws IllegalAccessException se non il metodo non ha accesso alla definizione di un item
     * @throws ItemInMultipleRoomsException se un item viene inserito in più stanze (nel caso dei link più di due stanze)
     * @throws ItemAlreadyCreated se viene creato un item con il nome di uno già
     * @throws RoomWithoutLinksException se viene creata una stanza senza nemmeno un link
     */
    public static Mondo fromFile(String percorso) throws IOException, WronglyFormattedFileException, RoomNotPresentException, RoomWithoutDescriptionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ItemInMultipleRoomsException, ItemAlreadyCreated, RoomWithoutLinksException {
        return fromFile(Path.of(percorso));
    }

    /**
     * Crea un mondo a partire da un file .game.
     *
     * @param path - {@link Path} del percorso del file
     * @return un mondo di gioco
     * @throws IOException se occorre un errore I/O leggendo il file
     * @throws WronglyFormattedFileException se il file .game non è formattato correttamente
     * @throws RoomNotPresentException se un link prova a collegare una stanza non presente
     * @throws RoomWithoutDescriptionException se non viene fornita la descrizione di una stanza
     * @throws ClassNotFoundException se non viene trovata la classe corrispondente a un item
     * @throws InvocationTargetException se la costruzione di un item solleva un eccezione
     * @throws NoSuchMethodException se il costruttore di un item non viene trovato
     * @throws InstantiationException se un item non può essere creato
     * @throws IllegalAccessException se non il metodo non ha accesso alla definizione di un item
     * @throws ItemInMultipleRoomsException se un item viene inserito in più stanze (nel caso dei link più di due stanze)
     * @throws ItemAlreadyCreated se viene creato un item con il nome di uno già presente
     * @throws RoomWithoutLinksException se viene creata una stanza senza nemmeno un link
     */
    public static Mondo fromFile(Path path) throws IOException, WronglyFormattedFileException, RoomNotPresentException, RoomWithoutDescriptionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ItemInMultipleRoomsException, ItemAlreadyCreated, RoomWithoutLinksException {
        String sectionType, sectionInfo = null, playerText = null, start = null;
        List<String> charactersText = null, objectsText = null, linksText = null, text = Files.readAllLines(path);
        Map<String, List<String>> roomsText = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        List<List<String>> sections = sections(text);
        String[] sectionTitle;

        for (List<String> section : sections) {
            sectionTitle = section.get(0).replace("]", "").replace("[", "").split(":");
            sectionType = sectionTitle[0];
            section.remove(0);
            if (sectionTitle.length > 1) sectionInfo = sectionTitle[1];
            switch (sectionType) {
                case "world" -> {
                    String name = null, description = null;
                    for (String line : section) {
                        name = sectionInfo;
                        if (line.startsWith("description")) description = line.split("\t")[1];
                        else if (line.startsWith("start")) start = line.split("\t")[1];
                    }
                    INSTANCE = new Mondo(name, description);
                }
                case "characters" -> charactersText = section;
                case "links" -> linksText = section;
                case "objects" -> objectsText = section;
                case "player" -> playerText = section.get(0);
                case "room" -> {
                    if (roomsText.get(sectionInfo) != null) throw new ItemAlreadyCreated(sectionInfo);
                    roomsText.put(sectionInfo, section);
                }
            }
        }

        if (linksText == null || objectsText == null || charactersText == null || playerText == null || roomsText.isEmpty()) {
            throw new WronglyFormattedFileException();
        }

        try {
            INSTANCE.readLinks(linksText);
            INSTANCE.readObjects(objectsText);
            INSTANCE.readCharacters(charactersText);
            INSTANCE.readRooms(roomsText);
            INSTANCE.initLinks();
            INSTANCE.createPlayer(playerText, start);
            return INSTANCE;
        } catch (NullPointerException e) {
            throw new WronglyFormattedFileException(); // se manca la sezione "world"
        }
    }

    private void initLinks() throws RoomNotPresentException {
        Stanza s1, s2;
        for (Link link : LINKS.values()) {
            s1 = STANZE.get(link.getSTANZA1());
            s2 = STANZE.get(link.getSTANZA2());
            if (s1 == null || s2 == null) throw new RoomNotPresentException();
            link.init(s1, s2);
        }
    }

    private static List<List<String>> sections(List<String> text) {
        List<List<String>> sections = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        String line;
        int k;
        for (int i = 0; i < text.size(); i++) {
            line = text.get(i);
            k = line.indexOf("//");
            if (k >= 0) line = line.substring(0, k); // rimuove commenti
            line = line.strip();
            if (line.startsWith("[") && i > 0) {
                sections.add(temp);
                temp = new ArrayList<>();
            }
            if (line.length() > 0) temp.add(line);
        }
        sections.add(temp);
        return sections;
    }

    private void createPlayer(String playerText, String start) throws WronglyFormattedFileException {
        String[] info = playerText.split("\\t");
        if (info[0].equals("") || info.length < 2 || !info[1].equals("Giocatore")) throw new WronglyFormattedFileException();
        Giocatore.init(info[0], STANZE.get(start));
    }

    private Link createLink(String linkText) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, WronglyFormattedFileException {
        String[] linkInfo = linkText.split("\\t");
        if(linkInfo.length < 4) throw new WronglyFormattedFileException();
        String name = linkInfo[0], stanza1 = linkInfo[2], stanza2 = linkInfo[3];
        Class<?> c = Class.forName("it.uniroma1.textadv.links." + linkInfo[1]);
        Class<? extends Link> linkCls = c.asSubclass(Link.class);
        Constructor<? extends Link> constr = linkCls.getConstructor(String.class, String.class, String.class);
        return constr.newInstance(name, stanza1, stanza2);
    }

    private Oggetto createOggetto(String oggettoText) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, WronglyFormattedFileException {
        Oggetto oggetto;
        String[] oggettoInfo = oggettoText.split("\\t");
        String name = oggettoInfo[0], param;
        Link l;
        if(name.equals("") || oggettoInfo.length < 2) throw new WronglyFormattedFileException();
        Class<?> c = Class.forName("it.uniroma1.textadv.oggetti." + oggettoInfo[1]);
        Class<? extends Oggetto> oggettoCls = c.asSubclass(Oggetto.class);
        Constructor<? extends Oggetto> constr;
        if (oggettoInfo.length > 2) {
            param = oggettoInfo[2];
            constr = oggettoCls.getConstructor(String.class, String.class);
            oggetto = constr.newInstance(name, param);
            if (c.getSuperclass().equals(Class.forName("it.uniroma1.textadv.oggetti.Opener"))) {
                l = LINKS.get(param);
                if (l != null) // se oggetto è un opener di un link (param chiave in link)
                    l.lock((Opener) oggetto);
            }
        } else {
            constr = oggettoCls.getConstructor(String.class);
            oggetto = constr.newInstance(name);
        }
        return oggetto;
    }

    private Personaggio createPersonaggio(String personaggioText) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, WronglyFormattedFileException {
        Personaggio p;
        String[] personaggioInfo = personaggioText.split("\\t");
        String name = personaggioInfo[0];
        if(name.equals("") || personaggioInfo.length < 2) throw new WronglyFormattedFileException();
        Class<?> c = Class.forName("it.uniroma1.textadv.personaggi." + personaggioInfo[1]);
        Class<? extends Personaggio> linkCls = c.asSubclass(Personaggio.class);
        Constructor<? extends Personaggio> constr;
        if (personaggioInfo.length < 3) {
            constr = linkCls.getConstructor(String.class);
            p = constr.newInstance(name);
        } else {
            constr = linkCls.getConstructor(String.class, String[].class);
            p = constr.newInstance(name, Arrays.copyOfRange(personaggioInfo, 2, personaggioInfo.length));
        }
        return p;
    }

    private void readLinks(List<String> linksText) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ItemAlreadyCreated, WronglyFormattedFileException {
        Link link;
        for (String linkText : linksText) {
            link = createLink(linkText);
            if (getItem(link.getName()) != null) throw new ItemAlreadyCreated(link.getName());
            LINKS.put(link.getName(), link);
        }
    }

    private void readObjects(List<String> objectsText) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ItemAlreadyCreated, WronglyFormattedFileException {
        Oggetto oggetto;
        for (String oggettoText : objectsText) {
            oggetto = createOggetto(oggettoText);
            if (getItem(oggetto.getName()) != null) throw new ItemAlreadyCreated(oggetto.getName());
            OGGETTI.put(oggetto.getName(), oggetto);
        }
        Container container;
        Blocker blocker;
        Oggetto toPut;
        for (Oggetto o : OGGETTI.values()) {
            if (o instanceof Container) {
                container = (Container) o;
                toPut = OGGETTI.get(container.getContentName());
                if (toPut instanceof Storable) container.put((Storable) toPut);
            } else if (o instanceof Blocker) {
                blocker = (Blocker) o;
                blocker.block(LINKS.get(blocker.getToOpen()));
            }
        }
    }

    private void readCharacters(List<String> charactersText) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ItemAlreadyCreated, WronglyFormattedFileException {
        Personaggio personaggio;
        for (String personaggioText : charactersText) {
            personaggio = createPersonaggio(personaggioText);
            if (getItem(personaggio.getName()) != null) throw new ItemAlreadyCreated(personaggio.getName());
            PERSONAGGI.put(personaggio.getName(), personaggio);
        }
    }

    private void readRooms(Map<String, List<String>> roomsText) throws RoomWithoutDescriptionException, ItemInMultipleRoomsException, ItemAlreadyCreated, RoomWithoutLinksException {
        Map<String, Personaggio> charToIns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Map<String, Oggetto> objToIns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Map<String, Integer> linkToIns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        charToIns.putAll(PERSONAGGI);
        objToIns.putAll(OGGETTI);

        Stanza.Builder builder = null;
        String[] lineText, linkArray;
        List<String> text;
        Direzione dir;
        Link link;
        Personaggio pers;
        Oggetto ogg;
        int l;
        boolean hasNotlinks;

        for (String nome : roomsText.keySet()) {
            if (STANZE.get(nome) != null) throw new ItemAlreadyCreated(nome);
            hasNotlinks = true;
            text = roomsText.get(nome);
            for (String line : text) {
                if (line.startsWith("description")) {
                    builder = new Stanza.Builder(nome, line.split("\\t")[1]);
                    break;
                }
            }
            if (builder == null) throw new RoomWithoutDescriptionException();
            for (String line : text) {
                lineText = line.split("\\t");
                if (lineText.length > 1) {
                    switch (lineText[0]) {
                        case "objects" -> {
                            for (String oggetto : lineText[1].split(",")) {
                                ogg = objToIns.remove(oggetto.strip());
                                if (ogg == null) throw new ItemInMultipleRoomsException(oggetto);
                                builder.addOggetto(ogg);
                            }
                        }
                        case "characters" -> {
                            for (String personaggio : lineText[1].split(",")) {
                                pers = charToIns.remove(personaggio.strip());
                                if (pers == null) throw new ItemInMultipleRoomsException(personaggio);
                                builder.addPersonaggio(pers);
                            }
                        }
                        case "links" -> {
                            hasNotlinks = false;
                            for (String dirLink : lineText[1].split(",")) {
                                linkArray = dirLink.strip().split(":");
                                dir = Direzione.get(linkArray[0]);
                                link = LINKS.get(linkArray[1]);
                                if (link == null) {
                                    link = new Link(linkArray[1], nome, linkArray[1]);
                                    LINKS.put(linkArray[1], link);
                                    LINKS.put(nome, new Link(nome, linkArray[1], nome));
                                } else {
                                    l = linkToIns.merge(linkArray[1], 1, (o, n) -> o - n);
                                    if (l < 0) throw new ItemInMultipleRoomsException(linkArray[1]);
                                }
                                builder.addLink(dir, link);
                            }
                        }
                    }
                }
            }
            if (hasNotlinks) throw new RoomWithoutLinksException(nome);
            STANZE.put(nome, builder.build());
        }
    }
    // TODO controllo formato righe stanze e mondo
}
