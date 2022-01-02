package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.Direzione;
import it.uniroma1.textadv.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.exceptions.LanguageNotKnownException;

import java.util.Set;

public enum Language {

    EN(Set.of("a", "an", "the", "about", "above", "against", "across", "along", "around", "among", "before", "at",
            "behind", "beneath", "below", "beside", "beyond", "between", "by", "during", "down", "except", "from",
            "for", "inside", "in", "into", "near", "like", "of", "on", "to", "since", "toward", "under", "though", "up",
            "until", "upon", "within", "with"), Language::en),

    IT(Set.of("il", "lo", "la", "i", "gli", "le", "un", "uno", "una", /*"di", "del",*/ "dello", "della", "dei", "degli",
            "delle", "a", "al", "allo", "alla", "ai", "agli", "alle", "da", "dal", "dallo", "dalla", "dai", "dagli",
            "dalle", "in", "nel", "nello", "nella", "nei", "negli", "nelle", "su", "sul", "sullo", "sulla", "sui",
            "sugli", "sulle", "con", "dentro"), Language::it);

    private final Set<String> STOP_WORDS;

    private final ActionFactory actionFactory;

    Language(Set<String> stop_words, ActionFactory actionFactory) {
        STOP_WORDS = stop_words;
        this.actionFactory = actionFactory;
    }

    public Set<String> getSTOP_WORDS() {
        return STOP_WORDS;
    }

    public ActionFactory getActionFactory() {
        return actionFactory;
    }

    public static Language get(String name) throws LanguageNotKnownException {
        try {
            return Language.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LanguageNotKnownException();
        }
    }

    private static Action en(String name)  throws ActionNotKnownException {
        try {
            return ENAction.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ActionNotKnownException();
        }
    }

    private static Action it(String name) throws ActionNotKnownException {
        try {
            return ITAction.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ActionNotKnownException();
        }
    }
}
