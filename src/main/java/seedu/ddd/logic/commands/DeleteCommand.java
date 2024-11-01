package seedu.ddd.logic.commands;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import seedu.ddd.commons.core.index.Index;
import seedu.ddd.commons.util.ToStringBuilder;
import seedu.ddd.logic.Messages;
import seedu.ddd.logic.commands.exceptions.CommandException;
import seedu.ddd.model.Displayable;
import seedu.ddd.model.Model;
import seedu.ddd.model.contact.common.Contact;
import seedu.ddd.model.event.common.Event;

/**
 * Deletes a contact or event identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_DESCRIPTION = COMMAND_WORD + ": deletes a contact";
    public static final String COMMAND_USAGE = "usage: " + COMMAND_WORD + " INDEX";
    public static final String EXAMPLE_USAGE = "example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_USAGE = COMMAND_DESCRIPTION + "\n"
            + COMMAND_USAGE + "\n"
            + EXAMPLE_USAGE;

    public static final String MESSAGE_DELETE_CONTACT_SUCCESS = "Deleted Contact: %1$s";
    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Event: %1$s";
    public static final String MESSAGE_UNKNOWN_ITEM = "Unknown item displayed in list.";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        ObservableList<Displayable> lastShownList = model.getDisplayedList();

        if (targetIndex.getOneBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_DISPLAYED_INDEX_TOO_LARGE);
        }

        Displayable itemToDelete = lastShownList.get(targetIndex.getZeroBased());

        if (itemToDelete instanceof Contact contactToDelete) {
            model.deleteContact(contactToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_CONTACT_SUCCESS, Messages.format(contactToDelete)));
        } else if (itemToDelete instanceof Event eventToDelete) {
            model.deleteEvent(eventToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, Messages.format(eventToDelete)));
        } else {
            throw new CommandException(MESSAGE_UNKNOWN_ITEM);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
