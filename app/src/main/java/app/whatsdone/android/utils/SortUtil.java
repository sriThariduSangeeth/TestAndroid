package app.whatsdone.android.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;

public class SortUtil {

    public static Collection<? extends BaseEntity> sort(List<BaseEntity> tasks) {
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

    public static Comparator<BaseEntity> overdueTaskCompare =
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
