package app.whatsdone.android.utils;

import java.util.Date;

import app.whatsdone.android.model.Change;
import app.whatsdone.android.model.CheckListItem;
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.model.User;
import app.whatsdone.android.services.AuthServiceImpl;

public class ObjectComparer {

    public static LogEvent isEqual(Task original, Task task, String groupId) {
        User current = AuthServiceImpl.getCurrentUser();
        String currentUserId = current.getDocumentID();
        String currentUserName = current.getDisplayName();
        LogEvent event = new LogEvent();
        event.setDocumentID(task.getDocumentID());
        event.setGroupId(groupId);

        if (original != null) {
            if (!original.getTitle().equals(task.getTitle())) {
                event.getLogs().add(
                        new Change(
                                currentUserId,
                                currentUserName,
                                Change.ChangeType.TITLE_CHANGE, new Date(),
                                original.getTitle(), task.getTitle()));
            }
            if (!original.getDescription().equals(task.getDescription())) {
                event.getLogs().add(
                        new Change(
                                currentUserId,
                                currentUserName,
                                Change.ChangeType.DETAIL_CHANGE, new Date(),
                                original.getDescription(), task.getDescription()));
            }
            if (original.getStatus() != task.getStatus()) {
                event.getLogs().add(
                        new Change(
                                currentUserId,
                                currentUserName,
                                Change.ChangeType.STATUS_CHANGE, new Date(),
                                original.getStatus().name(), task.getStatus().name()));
            }

            if (original.isAcknowledged() != task.isAcknowledged()) {
                event.getLogs().add(
                        new Change(
                                currentUserId,
                                currentUserName,
                                Change.ChangeType.ACKNOWLEGDE_CHANGE, new Date(),
                                String.valueOf(original.isAcknowledged()), String.valueOf((task.isAcknowledged()))));
            }

            if (!original.getAssignedUser().equals(task.getAssignedUser())) {
                event.getLogs().add(
                        new Change(
                                currentUserId,
                                currentUserName,
                                Change.ChangeType.ASSIGNEE_CHANGE, new Date(),
                                original.getAssignedUser(), task.getAssignedUser()));
            }

            if (!DateUtil.isDateTimeEqual(original.getDueDate(), task.getDueDate())) {
                event.getLogs().add(
                        new Change(
                                currentUserId,
                                currentUserName,
                                Change.ChangeType.DUE_CHANGE, new Date(),
                                DateUtil.formatted(original.getDueDate(), null),
                                DateUtil.formatted(task.getDueDate(), null)));
            }

            if (original.getCheckList().size() != task.getCheckList().size()) {
                event.getLogs().add(
                        new Change(
                                currentUserId,
                                currentUserName,
                                Change.ChangeType.CHECKLIST_CHANGE, new Date(),
                                String.valueOf(original.getCheckList().size()),
                                String.valueOf(task.getCheckList().size())
                        ));
            } else {
                for (int i = 0; i < original.getCheckList().size(); i++) {
                    CheckListItem originalItem = original.getCheckList().get(i);
                    CheckListItem newItem = task.getCheckList().get(i);

                    if (!newItem.getTitle().equals(originalItem.getTitle())
                            || newItem.isCompleted() != originalItem.isCompleted()) {
                        event.getLogs().add(
                                new Change(
                                        currentUserId,
                                        currentUserName,
                                        Change.ChangeType.CHECKLIST_CHANGE, new Date(),
                                        String.valueOf(original.getCheckList().size()),
                                        String.valueOf(task.getCheckList().size())
                                ));
                        break;
                    }
                }
            }
        }

        return event;
    }

}
