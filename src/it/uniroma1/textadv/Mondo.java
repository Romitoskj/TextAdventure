package it.uniroma1.textadv;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


import it.uniroma1.textadv.exceptions.*;
import it.uniroma1.textadv.interfaces.Item;
import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.interfaces.Blocker;
import it.uniroma1.textadv.oggetti.interfaces.Container;
import it.uniroma1.textadv.oggetti.interfaces.Oggetto;
import it.uniroma1.textadv.oggetti.interfaces.Opener;
import it.uniroma1.textadv.personaggi.Giocatore;
import it.uniroma1.textadv.personaggi.interfaces.Personaggio;

/**
 * Rappresenta il mondo in cui si ambienta il gioco. Esso contiene le varie stanze, oggetti e personaggi.
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
     * @param percorso stringa del percorso del file
     * @return un mondo di gioco
     * @throws IOException                se occorre un errore I/O leggendo il file
     * @throws MalformedGameFileException se il file .game non è formattato correttamente
     */
    public static Mondo fromFile(String percorso) throws IOException, MalformedGameFileException {
        return fromFile(Path.of(percorso));
    }

    /**
     * Crea un mondo a partire da un file .game.
     *
     * @param path - {@link Path} del percorso del file
     * @return un mondo di gioco
     * @throws IOException                se occorre un errore I/O leggendo il file
     * @throws MalformedGameFileException se il file .game non è formattato correttamente
     */
    public static Mondo fromFile(Path path) throws IOException, MalformedGameFileException {
        if (!path.toString().endsWith(".game"))
            throw new IOException("The file '%s' is not a .game file.".formatted(path));

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
                    if (section.size() == 0) throw new MalformedGameFileException("World section is empty.");
                    for (String line : section) {
                        name = sectionInfo;
                        try {
                            if (line.startsWith("description")) description = line.split("\t")[1];
                            else if (line.startsWith("start")) start = line.split("\t")[1];
                        } catch (IndexOutOfBoundsException e) {
                            throw new MalformedGameFileException("World section without description or start.");
                        }
                    }
                    if (name == null || name.equals("") || description == null || description.equals("") || start == null || start.equals(""))
                        throw new MalformedGameFileException("World section has not all the information needed.");
                    INSTANCE = new Mondo(name, description);
                }
                case "characters" -> charactersText = section;
                case "links" -> linksText = section;
                case "objects" -> objectsText = section;
                case "player" -> playerText = section.get(0);
                case "room" -> {
                    if (roomsText.get(sectionInfo) != null)
                        throw new MalformedGameFileException("the room %s appears more than one time.".formatted(sectionInfo));
                    roomsText.put(sectionInfo, section);
                }
            }
        }

        if (linksText == null || objectsText == null || charactersText == null || playerText == null || roomsText.isEmpty()) {
            throw new MalformedGameFileException("The file must have all the world section.");
        }

        try {
            INSTANCE.readLinks(linksText);
            INSTANCE.readObjects(objectsText);
            INSTANCE.readCharacters(charactersText);
            INSTANCE.readRooms(roomsText);
            INSTANCE.initLinks();
            INSTANCE.createPlayer(playerText, start);
            return INSTANCE;
        } catch (NullPointerException e) { // se manca la sezione "world"
            throw new MalformedGameFileException("The file must have all the world section.");
        }
    }

    private void initLinks() throws MalformedGameFileException {
        Stanza s1, s2;
        for (Link link : LINKS.values()) {
            s1 = STANZE.get(link.getSTANZA1());
            s2 = STANZE.get(link.getSTANZA2());
            if (s1 == null || s2 == null) throw new MalformedGameFileException("Link must connect two existing rooms.");
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

    private void createPlayer(String playerText, String start) throws MalformedGameFileException {
        String[] info = playerText.split("\\t");
        if (info[0].equals("") || info.length < 2 || !info[1].equals("Giocatore"))
            throw new MalformedGameFileException("Player section is empty.");
        Giocatore.init(info[0], STANZE.get(start));
    }

    private Link createLink(String linkText) throws MalformedGameFileException {
        String[] linkInfo = linkText.split("\\t");
        if (linkInfo.length < 4)
            throw new MalformedGameFileException("The link '%s' has not all the information needed.".formatted(linkInfo[0]));
        String name = linkInfo[0], stanza1 = linkInfo[2], stanza2 = linkInfo[3], classe = "it.uniroma1.textadv.links." + linkInfo[1];
        Class<?> c;
        try {
            c = Class.forName(classe);
            Class<? extends Link> linkCls = c.asSubclass(Link.class);
            Constructor<? extends Link> constr = linkCls.getConstructor(String.class, String.class, String.class);
            return constr.newInstance(name, stanza1, stanza2);
        } catch (ClassNotFoundException e) {
            throw new MalformedGameFileException("The class '%s' does not exist".formatted(classe));
        } catch (InvocationTargetException e) {
            throw new MalformedGameFileException("The constructor of the class '%s' has thrown an exception".formatted(classe));
        } catch (NoSuchMethodException e) {
            throw new MalformedGameFileException("The constructor of class '%s' does not exist".formatted(classe));
        } catch (InstantiationException e) {
            throw new MalformedGameFileException("The class '%s' can not be instantiated".formatted(classe));
        } catch (IllegalAccessException e) {
            throw new MalformedGameFileException("This method has not access to the constructor of class '%s' ".formatted(classe));
        }
    }

    private Oggetto createOggetto(String oggettoText) throws MalformedGameFileException {
        String[] oggettoInfo = oggettoText.split("\\t");
        if (name.equals("") || oggettoInfo.length < 2)
            throw new MalformedGameFileException("The object '%s' has not all the information needed.".formatted(oggettoInfo[0]));
        String name = oggettoInfo[0], param, classe = "it.uniroma1.textadv.oggetti." + oggettoInfo[1];
        Link l;
        Oggetto oggetto;
        try {
            Class<?> c = Class.forName(classe);
            Class<? extends Oggetto> oggettoCls = c.asSubclass(Oggetto.class);
            Constructor<? extends Oggetto> constr;
            if (oggettoInfo.length > 2) {
                param = oggettoInfo[2];
                constr = oggettoCls.getConstructor(String.class, String.class);
                oggetto = constr.newInstance(name, param);
                if (c.getSuperclass().equals(Class.forName("it.uniroma1.textadv.oggetti.interfaces.Opener"))) {
                    l = LINKS.get(param);
                    if (l != null) // se oggetto è un opener di un link (param chiave in link)
                        l.lock((Opener) oggetto);
                }
            } else {
                constr = oggettoCls.getConstructor(String.class);
                oggetto = constr.newInstance(name);
            }
            return oggetto;
        } catch (ClassNotFoundException e) {
            throw new MalformedGameFileException("The class '%s' does not exist".formatted(classe));
        } catch (InvocationTargetException e) {
            throw new MalformedGameFileException("The constructor of the class '%s' has thrown an exception".formatted(classe));
        } catch (NoSuchMethodException e) {
            throw new MalformedGameFileException("The constructor of class '%s' does not exist".formatted(classe));
        } catch (InstantiationException e) {
            throw new MalformedGameFileException("The class '%s' can not be instantiated".formatted(classe));
        } catch (IllegalAccessException e) {
            throw new MalformedGameFileException("This method has not access to the constructor of class '%s' ".formatted(classe));
        }
    }

    private Personaggio createPersonaggio(String personaggioText) throws MalformedGameFileException {
        String[] personaggioInfo = personaggioText.split("\\t");
        if (name.equals("") || personaggioInfo.length < 2)
            throw new MalformedGameFileException("The character '%s' has not all the information needed.".formatted(personaggioInfo[0]));
        String name = personaggioInfo[0], classe = "it.uniroma1.textadv.personaggi." + personaggioInfo[1];
        Personaggio p;
        try {
            Class<?> c = Class.forName(classe);
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
        } catch (ClassNotFoundException e) {
            throw new MalformedGameFileException("The class '%s' does not exist".formatted(classe));
        } catch (InvocationTargetException e) {
            throw new MalformedGameFileException("The constructor of the class '%s' has thrown an exception".formatted(classe));
        } catch (NoSuchMethodException e) {
            throw new MalformedGameFileException("The constructor of class '%s' does not exist".formatted(classe));
        } catch (InstantiationException e) {
            throw new MalformedGameFileException("The class '%s' can not be instantiated".formatted(classe));
        } catch (IllegalAccessException e) {
            throw new MalformedGameFileException("This method has not access to the constructor of class '%s' ".formatted(classe));
        }
    }

    private void readLinks(List<String> linksText) throws MalformedGameFileException {
        Link link;
        for (String linkText : linksText) {
            link = createLink(linkText);
            if (getItem(link.getName()) != null)
                throw new MalformedGameFileException("The link '%s' appears more than one time in the links section.".formatted(link.getName()));
            LINKS.put(link.getName(), link);
        }
    }

    private void readObjects(List<String> objectsText) throws MalformedGameFileException {
        Oggetto oggetto;
        for (String oggettoText : objectsText) {
            oggetto = createOggetto(oggettoText);
            if (getItem(oggetto.getName()) != null)
                throw new MalformedGameFileException("The object '%s' appears more than one time in the objects section.".formatted(oggetto.getName()));
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

    private void readCharacters(List<String> charactersText) throws MalformedGameFileException {
        Personaggio personaggio;
        for (String personaggioText : charactersText) {
            personaggio = createPersonaggio(personaggioText);
            if (getItem(personaggio.getName()) != null)
                throw new MalformedGameFileException("The character '%s' appears more than one time in the characters section.".formatted(personaggio.getName()));
            PERSONAGGI.put(personaggio.getName(), personaggio);
        }
    }

    private void readRooms(Map<String, List<String>> roomsText) throws MalformedGameFileException {
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
        boolean hasNotLinks;

        for (String nome : roomsText.keySet()) {
            if (STANZE.get(nome) != null)
                throw new MalformedGameFileException("The room '%s' appears more than one time.".formatted(nome));
            hasNotLinks = true;
            text = roomsText.get(nome);
            for (String line : text) {
                if (line.startsWith("description")) {
                    try {
                        builder = new Stanza.Builder(nome, line.split("\\t")[1]);
                    } catch (IndexOutOfBoundsException e) {
                        throw new MalformedGameFileException("The room '%s' has not a description.".formatted(nome));
                    }
                    break;
                }
            }
            if (builder == null)
                throw new MalformedGameFileException("The room '%s' has not a description.".formatted(nome));
            for (String line : text) {
                lineText = line.split("\\t");
                if (lineText.length > 1) {
                    switch (lineText[0]) {
                        case "objects" -> {
                            for (String oggetto : lineText[1].split(",")) {
                                ogg = objToIns.remove(oggetto.strip());
                                if (ogg == null)
                                    throw new MalformedGameFileException("The object '%s' appears in more than one room.".formatted(oggetto));
                                builder = builder.addOggetto(ogg);
                            }
                        }
                        case "characters" -> {
                            for (String personaggio : lineText[1].split(",")) {
                                pers = charToIns.remove(personaggio.strip());
                                if (pers == null)
                                    throw new MalformedGameFileException("The object '%s' appears in more than one room.".formatted(personaggio));
                                builder = builder.addPersonaggio(pers);
                            }
                        }
                        case "links" -> {
                            hasNotLinks = false;
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
                                    if (l < 0)
                                        throw new MalformedGameFileException("The link '%s' appears in more than two rooms.".formatted(linkArray[1]));
                                }
                                builder = builder.addLink(dir, link);
                            }
                        }
                    }
                }
            }
            if (hasNotLinks) throw new MalformedGameFileException("The room '%s' has not links.".formatted(nome));
            STANZE.put(nome, builder.build());
        }
    }
}
