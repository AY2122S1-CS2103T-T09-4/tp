package seedu.fast.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.fast.commons.core.GuiSettings;
import seedu.fast.commons.core.LogsCenter;
import seedu.fast.logic.commands.Command;
import seedu.fast.logic.commands.CommandResult;
import seedu.fast.logic.commands.exceptions.CommandException;
import seedu.fast.logic.parser.FastParser;
import seedu.fast.logic.parser.exceptions.ParseException;
import seedu.fast.model.Model;
import seedu.fast.model.ReadOnlyFast;
import seedu.fast.model.person.Person;
import seedu.fast.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final FastParser fastParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        fastParser = new FastParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = fastParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveFast(model.getFast());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyFast getFast() {
        return model.getFast();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getFastFilePath() {
        return model.getFastFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}