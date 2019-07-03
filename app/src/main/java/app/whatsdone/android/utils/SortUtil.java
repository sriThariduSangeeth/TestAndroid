package app.whatsdone.android.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Change;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;

public class SortUtil {

    public static List<BaseEntity> sort(List<BaseEntity> tasks) {
        LocalState.getInstance().syncTasks(tasks);
        List<BaseEntity> unreadTasks = new ArrayList<>();
        List<BaseEntity> readTasks = new ArrayList<>();
        List<BaseEntity> doneTasks = new ArrayList<>();
        for (BaseEntity task : tasks) {
            if(((Task)task).getStatus() == Task.TaskStatus.DONE){
                doneTasks.add(task);
            }else {
                if(((Task)task).isUnreadTask())
                    unreadTasks.add(task);
                else
                    readTasks.add(task);
            }
        }

        Collections.sort(unreadTasks, overdueTaskCompare);
        Collections.sort(readTasks, overdueTaskCompare);

        unreadTasks.addAll(readTasks); 
        unreadTasks.addAll(doneTasks);

        return unreadTasks;
    }

    public static List<ExistUser> clean(Group group, List<ExistUser> users) {
        List<ExistUser> cleaned = new ArrayList<>();
        for (String member : group.getMembers()) {
            ExistUser user = null;
            for (ExistUser existUser : users) {
                if(member.equals(existUser.getPhoneNumber())) {
                    user = existUser;
                    break;
                }
            }
            if(user != null){
                cleaned.add(user);
            }else {
                user = new ExistUser();
                user.setPhoneNumber(member);
                user.setDisplayName(member);
                cleaned.add(user);
            }

        }
        return cleaned;
    }

    public static List<Change> sortChanges(List<Change> changes){
        Collections.sort(changes, byDate);
        return changes;
    }


    private static Comparator<Change> byDate =
            (change1, change2) -> change1.getDate().before(change2.getDate())? 1 :
                    DateUtil.isDateTimeEqual(change1.getDate(), change2.getDate()) ? 0 : -1;

    private static Comparator<BaseEntity> overdueTaskCompare =
            (entity1, entity2) -> {
                Task task1 = (Task)entity1;
                Task task2 = (Task)entity2;

                int task1Label = getStatusIndicatorText(task1);
                int task2Label = getStatusIndicatorText(task2);
                return task1Label == R.string.task_overdue ? -1 : task2Label == R.string.task_overdue ? 1
                        : task1Label == R.string.task_due_soon ? -1 : task2Label == R.string.task_due_soon ? 1
                        : 0;
            };

    public static int getStatusIndicatorText(@NonNull Task task) {
        Date today = DateUtil.getLastMinuteDate(new Date());

        if (DateUtil.isDateEqual(today, task.getDueDate()))
            return R.string.task_due_soon;
        else if ((today).after(task.getDueDate()))
            return R.string.task_overdue;
        return R.string.task_ontrack;
    }

    public static int getStatusIndicatorColor(@NonNull Task task) {
        Date today = DateUtil.getLastMinuteDate(new Date());

        if (DateUtil.isDateEqual(today, task.getDueDate()))
            return R.color.LightSalmonGold;
        else if ((today).after(task.getDueDate()))
            return R.color.lightRed;
        return R.color.LimeGreen;
    }
}
