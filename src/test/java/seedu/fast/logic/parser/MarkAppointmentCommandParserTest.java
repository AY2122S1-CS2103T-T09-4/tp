package seedu.fast.logic.parser;

import static seedu.fast.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_AMY;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_INPUT;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_TIME_AMY;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_VENUE_AMY;
import static seedu.fast.logic.parser.CliSyntax.PREFIX_APPOINTMENT;
import static seedu.fast.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.fast.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.fast.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.fast.commons.core.index.Index;
import seedu.fast.logic.commands.MarkAppointmentCommand;
import seedu.fast.model.person.Appointment;

public class MarkAppointmentCommandParserTest {
    private MarkAppointmentCommandParser parser = new MarkAppointmentCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = String.valueOf(targetIndex.getOneBased());
        MarkAppointmentCommand expectedCommand = new MarkAppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_AMY, VALID_APPOINTMENT_TIME_AMY, VALID_APPOINTMENT_VENUE_AMY));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAppointmentCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, MarkAppointmentCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void parse_additionalField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAppointmentCommand.MESSAGE_USAGE);

        assertParseFailure(parser, MarkAppointmentCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_APPOINTMENT + VALID_APPOINTMENT_INPUT,
                expectedMessage);
    }
}