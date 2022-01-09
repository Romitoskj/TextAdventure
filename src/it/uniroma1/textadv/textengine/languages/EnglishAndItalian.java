package it.uniroma1.textadv.textengine.languages;

import it.uniroma1.textadv.textengine.actions.ActionFactory;
import it.uniroma1.textadv.textengine.actions.ENAction;
import it.uniroma1.textadv.textengine.actions.ITAction;
import it.uniroma1.textadv.textengine.exceptions.LanguageNotKnownException;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

/**
 * Enumerazione che contiene la lingua italiana e inglese.
 */
public enum EnglishAndItalian implements Language {

    EN(ENAction.getFactory(),
            Set.of("a", "an", "the", "about", "above", "against", "across", "along", "around", "among", "before", "at",
                    "behind", "beneath", "below", "beside", "beyond", "between", "by", "during", "down", "except", "from",
                    "for", "inside", "in", "into", "near", "like", "of", "on", "to", "since", "toward", "under", "though", "up",
                    "until", "upon", "within", "with", "and"),
            Map.<String, String>ofEntries(
                    entry("start", """
                            You are  %s and your goal is to find the hidden treasure.

                            You can write "help" in any moment if you are struggling with the commands.
                            Write "look" to look around the room and start the adventure!"""),
                    entry("not_found_action", "Unrecognized action."),
                    entry("win", "Well done! You found the treasure!"),
                    entry("end", "Write \"quit\" to quit the game."),
                    entry("drop_succ", "Object dropped!"),
                    entry("quit", "quit"),
                    entry("arg_0", "This command does not accept arguments."),
                    entry("arg_1", "You can provide one argument only."),
                    entry("cmd_not_found", "There are no commands with that name..."),
                    entry("help_headers", "COMMAND\t\t|\t\tDESCRIPTION\\n\n"),
                    entry("greeting", "Goodbye!"),
                    entry("item_not_found", "There is nothing like that in this room..."),
                    entry("go_arg_0", "You have to specify where you want to go."),
                    entry("go_too_many_args", "You have to specify one argument only."),
                    entry("go_d_link_not_found", "There are no passages in that direction."),
                    entry("go_n_link_not_found", "There are no passages with that name."),
                    entry("go_succ", "You are in %s."),
                    entry("go_fail", "The passage %s is closed."),
                    entry("take_args_0", "You have to specify what you want to take."),
                    entry("take_too_many_args", "You have to specify two arguments only."),
                    entry("take_succ", "Object added to the inventory!"),
                    entry("take_not_storable", "You cannot take it..."),
                    entry("collect_not_char_cont", "%s is not a container nor a character."),
                    entry("take_from_char_succ", "%s gave you %s."),
                    entry("take_from_char_fail", "%s does not have anything like that..."),
                    entry("take_from_cont_closed", "%s is %s."),
                    entry("lit", "lit"),
                    entry("closed", "closed"),
                    entry("container_fail", "%s does not contain %s."),
                    entry("drop_args_0", "You have to specify the object you want to leave."),
                    entry("drop_too_many_args", "You can leave only one object at a time."),
                    entry("open_args_0", "You have to specify what you want to open."),
                    entry("open_not_loackable", "This object cannot be opened."),
                    entry("open_already_open", "This is already %s!"),
                    entry("broken", "broken"),
                    entry("opened", "opened"),
                    entry("open_args_1", "You need something to %s this object..."),
                    entry("break", "break"),
                    entry("open", "open"),
                    entry("open_too_many_args", "You have to specify only one object to %s the first one."),
                    entry("breaks", "break"),
                    entry("opens", "open"),
                    entry("not_found_in_inventory", "You do not have anything like this..."),
                    entry("open_wrong_opener", "This object cannot %s that..."),
                    entry("open_unlocked", "This is not locked, just %s it!"),
                    entry("break_it", "break"),
                    entry("open_it", "open"),
                    entry("fire_succ", "Quenched!"),
                    entry("break_succ", "Broken!"),
                    entry("open_succ", "Open!"),
                    entry("break_args_0", "You have to specify what you want to break."),
                    entry("break_too_many_args", "You have to specify two objects only, one to break and one to break the first one with."),
                    entry("break_not_breakable", "You cannot break %s."),
                    entry("speak_args_0", "You have to specify who you want to talk to."),
                    entry("speak_too_many_args", "You can only talk to one character at a time."),
                    entry("char_not_found", "There is nobody with that name here..."),
                    entry("pet_args_0", "You have to specify who you want to pet."),
                    entry("pet_too_many_args", "You can pet only one animal at a time."),
                    entry("pet_person", "It is not nice to pet somebody you do not know!"),
                    entry("give_few_args", "You have to specify what to give and to who."),
                    entry("give_too_many_args", "You just have to specify what to give and to who."),
                    entry("give_vendor", "%s gave you a bucket and clippers."),
                    entry("give_succ", "%s given to %s."),
                    entry("use_args_0", "You have to specify what you want to use."),
                    entry("use_args_1", "You have to specify what to use the object on."),
                    entry("use_too_many_args", "You can specify two objects only."),
                    entry("use_not_found", "There is nothing with that name which you can use the object on."),
                    entry("use_fail", "It looks like it doesn’t do much..."),
                    entry("use_not_found_inventory", "You have nothing like that and there is not a passage with that name."),
                    entry("fill_succ", "Bucket filled!"),
                    entry("fill_fail", "You can only fill the bucket in the well...")
            )
    ),

    IT(ITAction.getFactory(),
            Set.of("il", "lo", "la", "i", "gli", "le", "un", "uno", "una", "di", "del", "dello", "della", "dei", "degli",
                    "delle", "a", "al", "allo", "alla", "ai", "agli", "alle", "da", "dal", "dallo", "dalla", "dai", "dagli",
                    "dalle", "in", "nel", "nello", "nella", "nei", "negli", "nelle", "su", "sul", "sullo", "sulla", "sui",
                    "sugli", "sulle", "con", "dentro", "e"),
            Map.<String, String>ofEntries(
                    entry("start", """
                            Sei %s e il tuo obiettivo è trovare il tesoro nascosto.

                            In ogni momento potrai scrivere "aiuto" se hai bisogno di una mano con i comandi.
                            Scrivi "guarda" per guardarti intorno e iniziare l'avventura!"""),
                    entry("not_found_action", "Azione non riconosciuta."),
                    entry("win", "Congratulazioni! Hai trovato il tesoro!"),
                    entry("end", "Scrivi \"esci\" per uscire dal gioco."),
                    entry("quit", "esci"),
                    entry("arg_0", "Questo comando non accetta argomenti."),
                    entry("arg_1", "Puoi fornire al massimo un argomento."),
                    entry("cmd_not_found", "Non c'è nessun comando chiamato così..."),
                    entry("help_headers", "COMANDO\t\t|\t\tDESCRIZIONE\n\n"),
                    entry("greeting", "Arrivederci!"),
                    entry("item_not_found", "In questa stanza non c'è nulla del genere..."),
                    entry("go_arg_0", "Devi specificare dove vuoi andare."),
                    entry("go_too_many_args", "Devi specificare un solo argomento."),
                    entry("go_d_link_not_found", "Non c'è nessun passaggio in questa direzione."),
                    entry("go_n_link_not_found", "Non c'è nessun passaggio chiamato così."),
                    entry("go_succ", "Sei in %s."),
                    entry("go_fail", "Il passaggio %s è chiuso."),
                    entry("take_args_0", "Devi specificare cosa vuoi prendere."),
                    entry("take_too_many_args", "Devi specificare massimo due argomenti."),
                    entry("take_succ", "Oggetto aggiunto all'inventario!"),
                    entry("take_not_storable", "Non puoi prenderlo..."),
                    entry("collect_not_char_cont", "%s non è né un contenitore né un personaggio."),
                    entry("take_from_char_succ", "%s ti ha dato %s."),
                    entry("take_from_char_fail", "%s non ha nulla del genere..."),
                    entry("take_from_cont_closed", "%s è %s."),
                    entry("lit", "acceso"),
                    entry("closed", "chiuso"),
                    entry("container_fail", "%s non contiene %s."),
                    entry("drop_args_0", "Devi specificare l'oggetto da lasciare."),
                    entry("drop_too_many_args", "Puoi lasciare solo un oggetto per volta."),
                    entry("drop_succ", "Oggetto lasciato!"),
                    entry("open_args_0", "Devi specificare cosa vuoi aprire."),
                    entry("open_not_loackable", "Non è possibile aprire quest'oggetto."),
                    entry("open_already_open", "E' già %s!"),
                    entry("broken", "rotto"),
                    entry("opened", "aperto"),
                    entry("open_args_1", "Serve qualcosa per %s quest'oggetto..."),
                    entry("break", "rompere"),
                    entry("open", "aprire"),
                    entry("open_too_many_args", "Devi specificare un solo oggetto per %s il primo."),
                    entry("breaks", "rompe"),
                    entry("opens", "apre"),
                    entry("not_found_in_inventory", "Non hai nulla del genere..."),
                    entry("open_wrong_opener", "Non si %s con quest'oggetto..."),
                    entry("open_unlocked", "Non è bloccato, %s e basta!"),
                    entry("break_it", "rompilo"),
                    entry("open_it", "aprilo"),
                    entry("fire_succ", "Spento!"),
                    entry("break_succ", "Rotto!"),
                    entry("open_succ", "Aperto!"),
                    entry("break_args_0", "Devi specificare cosa vuoi rompere."),
                    entry("break_too_many_args", "Devi specificare solo due oggetti, uno da rompere e uno con cui rompere il primo."),
                    entry("break_not_breakable", "%s non può essere rotto."),
                    entry("speak_args_0", "Devi specificare con chi vuoi parlare."),
                    entry("speak_too_many_args", "Puoi parlare solo a un personaggio per volta."),
                    entry("char_not_found", "Non c'è nessuno chiamato così qui..."),
                    entry("pet_args_0", "Devi specificare chi vuoi accarezzare."),
                    entry("pet_too_many_args", "Puoi accarezzare solo un animale per volta."),
                    entry("pet_person", "Non è carino accarezzare una persona che non conosci!"),
                    entry("give_few_args", "Devi specificare cosa vuoi dare e a chi."),
                    entry("give_too_many_args", "Devi specificare solamente cosa dare e a chi."),
                    entry("give_vendor", "%s ti ha dato secchio e tronchesi."),
                    entry("give_succ", "%s dato a %s."),
                    entry("use_args_0", "Devi specificare cosa vuoi usare."),
                    entry("use_args_1", "Devi specificare su cosa usare l'oggetto."),
                    entry("use_too_many_args", "Puoi specificare massimo due oggetti."),
                    entry("use_not_found", "Non c'è nulla chiamato così su cui usare l'oggetto."),
                    entry("use_fail", "Sembra non faccia nulla..."),
                    entry("use_not_found_inventory", "Non hai nulla del genere e non c'è un passaggio chiamato così..."),
                    entry("fill_succ", "Secchio riempito!"),
                    entry("fill_fail", "Puoi riempire il secchio solo al pozzo...")

            )
    );

    /**
     * Insieme delle stop word.
     */
    private final Set<String> STOP_WORDS;

    /**
     * Mappa dei riscontri testuali delle azioni.
     */
    private final Map<String, String> ANSWERS;

    /**
     * {@link ActionFactory} delle azioni relative alla lingua.
     */
    private final ActionFactory actionFactory;

    EnglishAndItalian(ActionFactory actionFactory, Set<String> stop_words, Map<String, String> answers) {
        STOP_WORDS = stop_words;
        this.actionFactory = actionFactory;
        ANSWERS = answers;
    }

    @Override
    public Set<String> getSTOP_WORDS() {
        return STOP_WORDS;
    }

    @Override
    public ActionFactory getActionFactory() {
        return actionFactory;
    }

    @Override
    public String getAnswer(String key) {
        return ANSWERS.get(key);
    }

    /**
     * Restituisce la {@link LanguageFactory} che può restituire la lingua italiana e inglese.
     *
     * @return la language factory
     */
    public static LanguageFactory getFactory() {
        return languageName -> {
            try {
                return EnglishAndItalian.valueOf(languageName.toUpperCase().strip());
            } catch (IllegalArgumentException e) {
                throw new LanguageNotKnownException();
            }
        };
    }
}
