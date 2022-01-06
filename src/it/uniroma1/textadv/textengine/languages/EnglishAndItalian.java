package it.uniroma1.textadv.textengine.languages;

import it.uniroma1.textadv.exceptions.LanguageNotKnownException;
import it.uniroma1.textadv.personaggi.Giocatore;
import it.uniroma1.textadv.textengine.actions.ActionFactory;
import it.uniroma1.textadv.textengine.actions.ENAction;
import it.uniroma1.textadv.textengine.actions.ITAction;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public enum EnglishAndItalian implements Language {

    EN(ENAction.getFactory(),
            Set.of("a", "an", "the", "about", "above", "against", "across", "along", "around", "among", "before", "at",
            "behind", "beneath", "below", "beside", "beyond", "between", "by", "during", "down", "except", "from",
            "for", "inside", "in", "into", "near", "like", "of", "on", "to", "since", "toward", "under", "though", "up",
            "until", "upon", "within", "with"),
            Map.ofEntries(
                    entry("start", """
                            You are  %s and your goal is to find the hidden treasure.

                            You can write "help" in any moment if your struggle with the commands.
                            Write "look" to look around the room and start the adventure!"""),
                    entry("not_found_action", "Unrecognized action."),
                    entry("win", "Well done! You found the treasure!"),
                    entry("end", "Write \"quit\" to quit the game."),
                    entry("quit", "quit")
            )
    ),

    IT(ITAction.getFactory(),
            Set.of("il", "lo", "la", "i", "gli", "le", "un", "uno", "una", "di", "del", "dello", "della", "dei", "degli",
            "delle", "a", "al", "allo", "alla", "ai", "agli", "alle", "da", "dal", "dallo", "dalla", "dai", "dagli",
            "dalle", "in", "nel", "nello", "nella", "nei", "negli", "nelle", "su", "sul", "sullo", "sulla", "sui",
            "sugli", "sulle", "con", "dentro"),
            Map.ofEntries(
                    entry("start", """
                            Sei %s e il tuo obbiettivo è trovare il tesoro nascosto.

                            In ogni momento potrai scrivere "aiuto" se hai bisogno di una mano con i comandi.
                            Scrivi "guarda" per guardarti intorno e iniziare l'avventura!"""),
                    entry("not_found_action", "Azione non riconosciuta."),
                    entry("win", "Congratulazioni! Hai trovato il tesoro!"),
                    entry("end", "Scrivi \"esci\" per uscire dal gioco."),
                    entry("quit", "esci")
            )
    );

    private final Set<String> STOP_WORDS;

    private final Map<String, String> ANSWERS;

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

    public static LanguageFactory getFactory() {
        return languageName -> {
            try {
                return EnglishAndItalian.valueOf(languageName.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new LanguageNotKnownException();
            }
        };
    }
}
