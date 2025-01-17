package seedu.fast.model;

import static java.util.Objects.requireNonNull;
import static seedu.fast.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.fast.commons.core.GuiSettings;
import seedu.fast.commons.core.LogsCenter;
import seedu.fast.model.person.Person;

/**
 * Represents the in-memory model of the FAST address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Fast fast;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given fast and userPrefs.
     */
    public ModelManager(ReadOnlyFast fast, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(fast, userPrefs);

        logger.fine("Initializing with FAST address book: " + fast + " and user prefs " + userPrefs);

        this.fast = new Fast(fast);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.fast.getPersonList());
    }

    public ModelManager() {
        this(new Fast(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getFastFilePath() {
        return userPrefs.getFastFilePath();
    }

    @Override
    public void setFastFilePath(Path fastFilePath) {
        requireNonNull(fastFilePath);
        userPrefs.setFastFilePath(fastFilePath);
    }

    //=========== FAST ================================================================================

    @Override
    public void setFast(ReadOnlyFast fast) {
        this.fast.resetData(fast);
    }

    @Override
    public ReadOnlyFast getFast() {
        return fast;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return fast.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        fast.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        fast.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        fast.setPerson(target, editedPerson);
    }

    @Override
    public void sortPerson(Comparator<Person> comparator) {
        requireNonNull(comparator);
        fast.sortPersons(comparator);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedFast}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return fast.equals(other.fast)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons);
    }

}
