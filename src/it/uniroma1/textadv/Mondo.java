package it.uniroma1.textadv;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


import it.uniroma1.textadv.exceptions.NotCreatedWorldException;
import it.uniroma1.textadv.links.Link;
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
	private final HashMap<String, Oggetto> OGGETTI = new HashMap<>();

	/**
	 * Dizionario contenente tutti i personaggi del mondo indicizzati con il loro nome.
	 */
	private final HashMap<String, Personaggio> PERSONAGGI = new HashMap<>();

	/**
	 * Dizionario contenente tutte le stanze del mondo indicizzate con il loro nome.
	 */
	private final HashMap<String, Stanza> STANZE = new HashMap<>();

	/**
	 * Dizionario contenente tutti i link del mondo indicizzati con il loro nome.
	 */
	private final HashMap<String, Link> LINKS = new HashMap<>();

	/**
	 * Crea un mondo di gioco a partire dal nome, dalla descrizione e dal nome della stanza di partenza.
	 *
	 * @param name Il nome del mondo.
	 * @param description La descrizione del mondo.
	 */
	private Mondo(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Restituisce il dizionario degli oggetti.
	 *
	 * @return dizionario degli oggetti.
	 */
	Map<String, Oggetto> getOggetti() {
		return OGGETTI;
	}

	/**
	 * Restituisce il dizionario dei personaggi.
	 *
	 * @return dizionario dei personaggi.
	 */
	Map<String, Personaggio> getPersonaggi() {
		return PERSONAGGI;
	}

	/**
	 * Restituisce il dizionario delle stanze.
	 *
	 * @return dizionario delle stanze.
	 */
	Map<String, Stanza> getStanze() {
		return STANZE;
	}

	/**
	 * Restituisce il dizionario dei link.
	 *
	 * @return dizionario dei link.
	 */
	Map<String, Link> getLinks() {
		return LINKS;
	}

	/**
	 * Restituisce un oggetto presente nel mondo di gioco dato il nome.
	 *
	 * @return oggetto richiesto.
	 */
	public Oggetto getOggetto(String oggetto) {
		return OGGETTI.get(oggetto);
	}

	/**
	 * Restituisce un personaggio presente nel mondo di gioco dato il nome.
	 *
	 * @return il personaggio richiesto.
	 */
	public Personaggio getPersonaggio(String personaggio) {
		return PERSONAGGI.get(personaggio);
	}

	/**
	 * Restituisce una stanza presente nel mondo di gioco dato il nome.
	 *
	 * @return la stanza richiesta.
	 */
	public Stanza getStanza(String stanza) {
		return STANZE.get(stanza);
	}

	/**
	 * Restituisce il dizionario dei link.
	 *
	 * @return dizionario dei link.
	 */
	public Link getLink(String link) {
		return LINKS.get(link);
	}

	/**
	 * Restituisce l'istanza del mondo
	 *
	 * @return istanza del mondo
	 */
	public static Mondo getInstance() {
		if (INSTANCE == null)
			throw new NotCreatedWorldException();
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
	 */
	public static Mondo fromFile(String percorso) throws IOException {
		return fromFile(Path.of(percorso));
	}

	/**
	 * Crea un mondo a partire da un file .game.
	 *
	 * @param path - {@link Path} del percorso del file
	 * @return un mondo di gioco
	 * @throws IOException - se occorre un errore I/O leggendo il file
	 */
	public static Mondo fromFile(Path path) throws IOException {
		String sectionType, sectionInfo = null, playerText = null, start = null;
		List<String> charactersText = new ArrayList<>(), objectsText = new ArrayList<>(), linksText = new ArrayList<>(), text = Files.readAllLines(path);
		Map<String, List<String>> roomsText = new HashMap<>();
		List<List<String>> sections = sections(text);
		String[] sectionTitle;

		for (List<String> section: sections) {
			sectionTitle = section.get(0).replace("]","").replace("[","").split(":");
			sectionType = sectionTitle[0];
			section.remove(0);
			if (sectionTitle.length > 1) sectionInfo = sectionTitle[1];
			switch (sectionType) {
				case "world" -> {
					String name = null, description = null;
					for (String line : section) {
						name = sectionInfo;
						if (line.startsWith("description"))
							description = line.split("\t")[1];
						else if (line.startsWith("start"))
							start = line.split("\t")[1];
					}
					INSTANCE = new Mondo(name, description);
				}
				case "characters" -> charactersText = section;
				case "links" -> linksText = section;
				case "objects" -> objectsText = section;
				case "player" -> playerText = section.get(0);
				case "room" -> roomsText.put(sectionInfo, section);
			}
		}

		INSTANCE.readLinks(linksText);
		INSTANCE.readObjects(objectsText);
		INSTANCE.readCharacters(charactersText);
		INSTANCE.readRooms(roomsText);
		INSTANCE.createPlayer(playerText, start);
		return INSTANCE;
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
			if (line.startsWith("[") && i > 0 ) {
				sections.add(temp);
				temp = new ArrayList<>();
			}
			if (line.length() > 0) temp.add(line);
		}
		sections.add(temp);
		return sections;
	}

	private void createPlayer(String playerText, String start) {
		Giocatore.init(playerText.split("\\t")[0], STANZE.get(start));
	}

	private Link createLink(String linkText) {
		Link link = null;
		String[] linkInfo = linkText.split("\\t");
		String name = linkInfo[0], stanza1 = linkInfo[2], stanza2 = linkInfo[3];
		try {
			Class<?> c = Class.forName("it.uniroma1.textadv.links." + linkInfo[1]);
			Class<? extends Link> linkCls = c.asSubclass(Link.class);
			Constructor<? extends Link> constr = linkCls.getConstructor(String.class, String.class, String.class);
			link = constr.newInstance(name, stanza1, stanza2);
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return link;
	}

	private Oggetto createOggetto(String oggettoText) {
		Oggetto oggetto = null;
		String[] oggettoInfo = oggettoText.split("\\t");
		String name = oggettoInfo[0], param;
		Link l;
		try {
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
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return oggetto;
	}

	private Personaggio createPersonaggio(String personaggioText) {
		Personaggio p = null;
		String[] personaggioInfo = personaggioText.split("\\t");
		String name = personaggioInfo[0];
		try {
			Class<?> c = Class.forName("it.uniroma1.textadv.personaggi." + personaggioInfo[1]);
			Class<? extends Personaggio> linkCls = c.asSubclass(Personaggio.class);
			Constructor<? extends Personaggio> constr;
			if (personaggioInfo.length <= 2) {
				constr = linkCls.getConstructor(String.class);
				p = constr.newInstance(name);
			} else {
				constr = linkCls.getConstructor(String.class, String[].class);
				p = constr.newInstance(name, Arrays.copyOfRange(personaggioInfo, 2, personaggioInfo.length));
			}
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return p;
	}

	private void readLinks(List<String> linksText) {
		Link link;
		for (String linkText : linksText) {
			link = createLink(linkText);
			LINKS.put(link.getNome(), link);
		}
	}

	private void readObjects(List<String> objectsText) {
		Oggetto oggetto;
		for (String oggettoText : objectsText) {
			oggetto = createOggetto(oggettoText);
			OGGETTI.put(oggetto.getNome(), oggetto);
		}
	}

	private void readCharacters(List<String> charactersText) {
		Personaggio personaggio;
		for (String personaggioText : charactersText) {
			personaggio = createPersonaggio(personaggioText);
			PERSONAGGI.put(personaggio.getNome(), personaggio);
		}
		Container container;
		Oggetto toPut;
		for (Oggetto oggetto : OGGETTI.values()) {
			if (oggetto instanceof Container) {
				container = (Container) oggetto;
				toPut = OGGETTI.get(container.getContentName());
				if (toPut instanceof Storable) container.put((Storable) toPut);
			}
		}
	}

	private void readRooms(Map<String, List<String>> roomsText) {
		Stanza.Builder builder = null;
		String[] lineText, linkArray;
		List<String> text;
		Direzione dir;
		Link link;
		for (String nome: roomsText.keySet()) {
			text = roomsText.get(nome);
			for (String line: text) {
				if(line.startsWith("description")) {
					builder = new Stanza.Builder(nome, line.split("\\t")[1]);
					break;
				}
			}
			for (String line: text) {
				lineText = line.split("\\t");
				if (lineText.length > 1) {
					switch (lineText[0]) {
						case "objects" -> {
							for (String oggetto : lineText[1].split(","))
								builder.addOggetto(OGGETTI.get(oggetto.strip()));
						}
						case "characters" -> {
							for (String personaggio : lineText[1].split(","))
								builder.addPersonaggio(PERSONAGGI.get(personaggio.strip()));
						}
						case "links" -> {
							for (String dirLink : lineText[1].split(",")) {
								linkArray = dirLink.strip().split(":");
								dir = Direzione.get(linkArray[0]);
								link = LINKS.get(linkArray[1]);
								if (link == null) {
									link = new Link(linkArray[1], nome, linkArray[1]);
									LINKS.put(linkArray[1], link);
									LINKS.put(nome, new Link(nome, linkArray[1], nome));
								}
								builder.addLink(dir, link);
							}
						}
					}
				}
			}
			STANZE.put(nome, builder.build());
		}
	}
	// TODO Il metodo per il caricamento deve controllare (ed emettere il conseguente errore) se:
	//  lo stesso oggetto o personaggio viene inserito in più stanze,
	//  se gli oggetti specificati in ogni stanza sono sempre associati a una classe Java corrispondente,
	//  altri eventuali errori che possono rendere impossibile la creazione del mondo (ad es. due stanze od oggetti con
	//  lo stesso nome).
}
