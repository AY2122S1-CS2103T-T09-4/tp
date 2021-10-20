package seedu.fast.logic.commands;

import static seedu.fast.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.fast.commons.core.Messages;
import seedu.fast.commons.core.index.Index;
import seedu.fast.logic.commands.exceptions.CommandException;
import seedu.fast.model.Model;
import seedu.fast.model.person.Person;
import seedu.fast.model.tag.Tag;

/**
 * Edits the tag(s) of an existing person in FAST.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds to or deletes the tag(s) of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing tags will not be overwritten by the input.\n\n"
            + "Parameters: \nINDEX (must be a postive integer) "
            + "a/[TAGNAME]\n"
            + "d/[TAGNAME]\n\n"
            + "Example: \n" + COMMAND_WORD + " 1 "
            + "a/Friend d/Family";
    public static final String MESSAGE_ADD_TAG_SUCCESS = "Edited tags of Person: %1$s";
    public static final String MESSAGE_TAGS_ARE_REPEATED = "A tag with the name %1$s already exists!";
    public static final String MESSAGE_TAG_DOES_NOT_EXIST = "The tag %1$s does not exist!";
    public static final String MESSAGE_MULTIPLE_PRIORITY_TAGS = "Each person can only have one Priority tag!";

    private final Index index;
    private Set<Tag> addTags;
    private Set<Tag> deleteTags;

    /**
     * Construct for a {@code RemarkCommand}
     *
     * @param index index of the person in the filtered person list to add the tags
     * @param addTags Tags of the person to be added
     * @param deleteTags Tags of the person to be deleted
     */
    public TagCommand(Index index, Set<Tag> addTags, Set<Tag> deleteTags) {
        requireAllNonNull(index, addTags, deleteTags);

        this.index = index;
        this.addTags = addTags;
        this.deleteTags = deleteTags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> oldTags = personToEdit.getTags();
        Set<Tag> newTags = new HashSet<>(oldTags);

        for (Tag tag: deleteTags) {
            if (!newTags.remove(tag)) {
                throw new CommandException(String.format(MESSAGE_TAG_DOES_NOT_EXIST, tag.tagName));
            }
        }

        for (Tag tag: addTags) {
            if (!newTags.add(tag)) {
                throw new CommandException(String.format(MESSAGE_TAGS_ARE_REPEATED, tag.tagName));
            }
        }

        //add any additional tag constraint checks here, for priority and investment plan tags
        if (TagCommandUtils.hasMultiplePriorityTags(newTags)) {
            throw new CommandException(MESSAGE_MULTIPLE_PRIORITY_TAGS);
        }

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getRemark(), newTags,
                personToEdit.getAppointment(), personToEdit.getCount());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether
     * the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        return String.format(MESSAGE_ADD_TAG_SUCCESS, personToEdit);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        // state check
        TagCommand e = (TagCommand) other;
        return index.equals(e.index) && addTags.equals(e.addTags) && deleteTags.equals(e.deleteTags);
    }

}
