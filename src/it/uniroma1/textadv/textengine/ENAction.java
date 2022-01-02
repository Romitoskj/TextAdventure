package it.uniroma1.textadv.textengine;

import java.util.List;
import java.util.function.BiFunction;

public enum ENAction implements Action {

    HELP(Action::help, "\t\t\tDisplay the actions list or a specific action description."),

    QUIT(Action::quit, "\t\t\tQuit game."),

    INVENTORY(Action::inventory, "\t\tDisplay your inventory."),

    LOOK(Action::look, "\t\t\tDescribes the provided object or, if it is not provided, the whole room you are."),

    GO(Action::go, "\t\t\t\tMakes you move among the world rooms. You need to provide the passage name or direction (north, south, est or west)."),

    ENTER(Action::enter, "\t\t\tMakes you go through a provided passage."),

    TAKE(Action::take, "\t\t\tTakes something from the room you are, from inside an object or from a character. You will find what you took in the inventory."),

    DROP(Action::drop, "\t\t\tDrops an object that you have in the inventory in the room you are."),

    GIVE(Action::give, "\t\t\tGives an object you have to another character."),

    USE(Action::use, "\t\t\t\tUses an object you have or that is in the room you are."),

    OPEN(Action::open, "\t\t\tOpens a closed passage or object. If it is blocked you have to provide an object to open it."),

    BREAK(Action::breakItem, "\t\t\tBreaks an object that is in the room you are (not all objects can be broken)."),

    SPEAK(Action::speak, "\t\t\tSpeaks with another characters."),

    PET(Action::speak, "\t\t\t\tPets an animal.");

    private final String DESCRIPTION;

    private final BiFunction<List<String>, Language, String> method;

    ENAction(BiFunction<List<String>, Language, String> method, String description) {
        this.method = method;
        DESCRIPTION = description;
    }

    @Override
    public String getDescription() {
        return this + DESCRIPTION;
    }

    @Override
    public String execute(List<String> args) {
        return method.apply(args, Language.EN);
    }
}
