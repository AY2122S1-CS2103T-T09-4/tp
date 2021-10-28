package seedu.fast.logic.commands;

import seedu.fast.commons.core.Messages;
import seedu.fast.commons.core.index.Index;
import seedu.fast.logic.commands.exceptions.CommandException;
import seedu.fast.model.Model;
import seedu.fast.model.person.Appointment;
import seedu.fast.model.person.AppointmentCount;
import seedu.fast.model.person.Person;

import java.util.List;

import static seedu.fast.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.fast.model.Model.PREDICATE_SHOW_ALL_PERSONS;

public class UnmarkAppointmentCommand extends Command{
    public static final String COMMAND_WORD = "ua";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undo marking an appointment with the clients specified"
            + " by the index number used in the last client listing.\n\n"
            + "Parameters: \nINDEX (must be a positive integer)\n\n"
            + "Example: \n" + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_UNMARK_APPOINTMENT_SUCCESS = "Successfully undo marking of appointment with "
            + "%1$s.";
    public static final String MESSAGE_UNMARK_APPOINTMENT_FAILURE_ZERO = "You cannot undo marking of appointment "
            + "if you have not done any appointment!";

    public static final String MESSAGE_UNMARK_APPOINTMENT_FAILURE_EXIST = "You cannot undo marking of appointment "
            + "if you have a scheduled appointment with %1$s currently!";

    private final Index index;
    private final Appointment appointment;

    /**
     * Construct for an {@code UnmarkAppointmentCommand}
     *
     * @param index index of the person in the filtered person list to mark the appointment as completed
     * @param appointment the existing appointment that has been marked completed
     */
    public UnmarkAppointmentCommand(Index index, Appointment appointment) {
        requireAllNonNull(index, appointment);

        this.index = index;
        this.appointment = appointment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // AppointmentCount cannot go below 0.
        if (!AppointmentCount.isValidDecrementCount(personToEdit.getCount())) {
            throw new CommandException(MESSAGE_UNMARK_APPOINTMENT_FAILURE_ZERO);
        }

        // Has an appointment -> means did not accidentally mark an appointment as completed.
        if (!(personToEdit.getAppointment().getDate().equalsIgnoreCase(Appointment.NO_APPOINTMENT))) {
            throw new CommandException(String.format(MESSAGE_UNMARK_APPOINTMENT_FAILURE_EXIST,
                    personToEdit.getName()));
        }

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getRemark(), personToEdit.getTags(), appointment,
                personToEdit.getCount().decrementAppointmentCount());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(personToEdit));
    }

    /**
     * Generates a command execution success message when the appointment is marked as completed successfully.
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {

        return String.format(MESSAGE_UNMARK_APPOINTMENT_SUCCESS, personToEdit.getName().fullName);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkAppointmentCommand)) {
            return false;
        }

        // state check
        UnmarkAppointmentCommand e = (UnmarkAppointmentCommand) other;
        return index.equals(e.index)
                && appointment.equals(e.appointment);
    }
}