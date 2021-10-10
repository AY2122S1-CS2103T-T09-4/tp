package seedu.fast.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.fast.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tags names should be alphanumeric, or follow the specified "
            + "format for a PriorityTag";

    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagTerm(String test) {
        return test.matches(VALIDATION_REGEX) || test.matches(PriorityTag.PRIORITY_VALIDATION_REGEX);
    }

    /**
     * Create either an instance of a Tag or a PriorityTag depending on the input term.
     */
    public static Tag createTag(String term) {
        if (term.matches(PriorityTag.PRIORITY_VALIDATION_REGEX)) {
            return new PriorityTag(term);
        }
        return new Tag(term);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
