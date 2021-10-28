package seedu.fast.logic.parser;

import seedu.fast.commons.core.index.Index;
import seedu.fast.logic.commands.UnmarkAppointmentCommand;
import seedu.fast.model.person.Appointment;

import org.junit.jupiter.api.Test;

import static seedu.fast.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_AMY;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_INPUT;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_TIME_AMY;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_VENUE_AMY;
import static seedu.fast.logic.parser.CliSyntax.PREFIX_APPOINTMENT;
import static seedu.fast.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.fast.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.fast.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

public class UnmarkAppointmentCommandParserTest {
    private UnmarkAppointmentCommandParser parser = new UnmarkAppointmentCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = String.valueOf(targetIndex.getOneBased());
        UnmarkAppointmentCommand expectedCommand = new UnmarkAppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_AMY, VALID_APPOINTMENT_TIME_AMY, VALID_APPOINTMENT_VENUE_AMY));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UnmarkAppointmentCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_additionalField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UnmarkAppointmentCommand.MESSAGE_USAGE);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_APPOINTMENT + VALID_APPOINTMENT_INPUT, expectedMessage);
    }
}