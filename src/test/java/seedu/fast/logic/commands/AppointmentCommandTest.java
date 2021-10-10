package seedu.fast.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_AMY;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_BOB;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_TIME_AMY;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_TIME_BOB;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_VENUE_AMY;
import static seedu.fast.logic.commands.CommandTestUtil.VALID_APPOINTMENT_VENUE_BOB;
import static seedu.fast.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.fast.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.fast.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.fast.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.fast.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.fast.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.fast.testutil.TypicalPersons.getTypicalFast;

import org.junit.jupiter.api.Test;

import seedu.fast.commons.core.Messages;
import seedu.fast.commons.core.index.Index;
import seedu.fast.model.Fast;
import seedu.fast.model.Model;
import seedu.fast.model.ModelManager;
import seedu.fast.model.UserPrefs;
import seedu.fast.model.person.Appointment;
import seedu.fast.model.person.Person;
import seedu.fast.testutil.PersonBuilder;

public class AppointmentCommandTest {
    private static final String APPOINTMENT_STUB = "10 Oct 2021";
    private static final String NO_APPOINTMENT_STUB = "No Appointment Scheduled Yet";

    private static final String TIME_STUB = "16:00";
    private static final String NO_TIME_STUB = "";

    private static final String VENUE_STUB = "Tampines Hub";
    private static final String NO_VENUE_STUB = "";

    private final Model model = new ModelManager(getTypicalFast(), new UserPrefs());

    @Test
    public void equals() {
        final AppointmentCommand standardCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_AMY, VALID_APPOINTMENT_TIME_AMY, VALID_APPOINTMENT_VENUE_AMY));

        // same values -> returns true
        AppointmentCommand commandWithSameValues = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_AMY, VALID_APPOINTMENT_TIME_AMY, VALID_APPOINTMENT_VENUE_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AppointmentCommand(INDEX_SECOND_PERSON,
                new Appointment(VALID_APPOINTMENT_AMY, VALID_APPOINTMENT_TIME_AMY, VALID_APPOINTMENT_VENUE_AMY))));

        // different appointment -> returns false
        assertFalse(standardCommand.equals(new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_BOB, VALID_APPOINTMENT_TIME_BOB, VALID_APPOINTMENT_VENUE_BOB))));

        // different appointment date -> return false
        assertFalse(standardCommand.equals(new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(APPOINTMENT_STUB, VALID_APPOINTMENT_TIME_AMY, VALID_APPOINTMENT_VENUE_AMY))));

        //different appointment time -> return false
        assertFalse(standardCommand.equals(new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_AMY, TIME_STUB, VALID_APPOINTMENT_VENUE_AMY))));

        // different appointment venue -> return false
        assertFalse(standardCommand.equals(new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_AMY, VALID_APPOINTMENT_TIME_AMY, VENUE_STUB))));
    }

    // Unfiltered List Tests
    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AppointmentCommand apptCommand = new AppointmentCommand(outOfBoundIndex,
                new Appointment(VALID_APPOINTMENT_BOB, VALID_APPOINTMENT_TIME_BOB, VALID_APPOINTMENT_VENUE_BOB));

        assertCommandFailure(apptCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_deleteAppointmentUnfilteredList_success() {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(secondPerson)
                .withAppointment(NO_APPOINTMENT_STUB, NO_TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_SECOND_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(secondPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    // without time, without venue
    @Test
    public void execute_addAppointmentWithoutAdditionUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson)
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_THIRD_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithoutAdditionUnfilteredList_success() {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(secondPerson)
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_SECOND_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(secondPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    // with time, with venue
    @Test
    public void execute_addAppointmentWithAdditionUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson)
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithAdditionUnfilteredList_success() {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(secondPerson)
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_SECOND_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(secondPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }


    // with time, without venue
    @Test
    public void execute_addAppointmentWithTimeUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson)
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithTimeUnfilteredList_success() {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(secondPerson)
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_SECOND_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(secondPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    //without time, with venue
    @Test
    public void execute_addAppointmentWithVenueUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson)
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithVenueUnfilteredList_success() {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(secondPerson)
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_SECOND_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(secondPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    // Filtered List Test
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of FAST list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getFast().getPersonList().size());

        AppointmentCommand apptCommand = new AppointmentCommand(outOfBoundIndex,
                new Appointment(VALID_APPOINTMENT_BOB, VALID_APPOINTMENT_TIME_BOB, VALID_APPOINTMENT_VENUE_BOB));
        assertCommandFailure(apptCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_deleteAppointmentFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(NO_APPOINTMENT_STUB, NO_TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    // without time, without venue
    @Test
    public void execute_addAppointmentWithoutAdditionFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(),
                editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithoutAdditionFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    // with time, with venue
    @Test
    public void execute_addAppointmentWithAdditionFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(),
                editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithAdditionFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    // with time, without venue
    @Test
    public void execute_addAppointmentWithTimeFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(),
                editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithTimeFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, TIME_STUB, NO_VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    // without time, with venue
    @Test
    public void execute_addAppointmentWithVenueFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(),
                editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateAppointmentWithVenueFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(APPOINTMENT_STUB, NO_TIME_STUB, VENUE_STUB).build();
        Appointment editedAppt = editedPerson.getAppointment();

        AppointmentCommand appointmentCommand = new AppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue()));

        String expectedMessage = String.format(AppointmentCommand.MESSAGE_UPDATE_APPOINTMENT_SUCCESS,
                editedPerson.getName().fullName, editedAppt.getDate(), editedAppt.getTime(), editedAppt.getVenue());

        Model expectedModel = new ModelManager(new Fast(model.getFast()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(appointmentCommand, model, expectedMessage, expectedModel);
    }
}
