package seedu.fast.logic.commands;

import seedu.fast.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exits FAST and closes the window \n\n"
            + "Example: \n" + COMMAND_WORD;
    
    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting FAST as requested ...";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

}
