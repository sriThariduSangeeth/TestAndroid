package app.whatsdone.android.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import timber.log.Timber;

import static app.whatsdone.android.utils.Constants.DATETIME_FORMAT;

public class LocalState {
    private static final String TASK_COUNT = "task_count";
    private static final String TASK_COUNT_READ = "task_count_read";
    private static final String DISCUSSION_COUNT = "discussion_count";
    private static final String DISCUSSION_COUNT_READ = "discussion_count_read";

    private LocalState() {}

    private static class LazyHolder {
        static final LocalState INSTANCE = new LocalState();
    }
    public static LocalState getInstance() {
        return LazyHolder.INSTANCE;
    }

    private HashMap<String, HashMap<String, Serializable>> groupsData = new HashMap<>();
    private HashMap<String, String> taskData = new HashMap<>();
    private HashMap<String, HashMap<String, Serializable>> taskHistory = new HashMap<>();

    public void syncTasks(List<BaseEntity> tasks){
        for (BaseEntity entity: tasks) {
            Task task = (Task) entity;

            if(taskData.containsKey(task.getDocumentID())){
                Date date = DateUtil.parse(taskData.get(entity.getDocumentID()), DATETIME_FORMAT);
                if(date.after(task.getUpdatedDate())){
                    task.setUnreadTask(false);
                }
                else {
                    taskData.put(task.getDocumentID(), DateUtil.formatted(task.getUpdatedDate(), DATETIME_FORMAT));
                    task.setUnreadTask(true);
                }
            }else {
                task.setUnreadTask(true);
                taskData.put(task.getDocumentID(), DateUtil.formatted(task.getUpdatedDate(), DATETIME_FORMAT));
            }
        }
    }

    public void setTaskRead(Task task){
        taskData.put(task.getDocumentID(), DateUtil.formatted(new Date(), DATETIME_FORMAT));
        taskData.put(task.getDocumentID(), DateUtil.formatted(DateUtil.addTime(new Date(), 1), DATETIME_FORMAT));
    }

    public void syncGroups(List<BaseEntity> groups){
        boolean isFirstTime = false;
        if(groupsData.isEmpty())
            isFirstTime = true;
        for (BaseEntity entity: groups) {
            Group group = (Group)entity;
            int totalTaskCount = group.getTaskCount();
            int totalDiscussionCount = group.getDiscussionCount();
            Date updated = group.getUpdatedDate();
            String id =  group.getDocumentID();
            if(groupsData.containsKey(id)){
                HashMap data = groupsData.get(id);
                int taskCount = Integer.parseInt((String) data.get(TASK_COUNT_READ));
                int discussionCount = Integer.parseInt((String) data.get(DISCUSSION_COUNT_READ));

                groupsData.get(id).put(DISCUSSION_COUNT, String.valueOf(totalDiscussionCount));
                group.setUnreadDiscussionCount(Math.max(totalDiscussionCount - discussionCount, 0));

                groupsData.get(id).put(TASK_COUNT, String.valueOf(totalTaskCount));

                group.setUnreadTaskCount(Math.max(totalTaskCount - taskCount, 0));

            }else {
                HashMap<String, Serializable> data = new HashMap<>();
                data.put(TASK_COUNT, String.valueOf(totalTaskCount));
                data.put(TASK_COUNT_READ,isFirstTime ? String.valueOf(totalTaskCount) : String.valueOf(0));
                data.put(DISCUSSION_COUNT,  String.valueOf(totalDiscussionCount));
                data.put(DISCUSSION_COUNT_READ,isFirstTime ? String.valueOf(totalDiscussionCount): String.valueOf(0));
                groupsData.put(id, data);
                group.setUnreadDiscussionCount(totalDiscussionCount);
                group.setUnreadTaskCount(totalTaskCount);
                group.setUnreadDiscussionCount(isFirstTime? 0 : totalDiscussionCount);
                group.setUnreadTaskCount(isFirstTime? 0 : totalTaskCount);
            }
        }
    }

    public void markTasksRead(String groupId, int count){
        if(groupsData.containsKey(groupId)) {
            groupsData.get(groupId).put(TASK_COUNT_READ, String.valueOf(count));
            groupsData.get(groupId).put(TASK_COUNT, String.valueOf(count));
        }
    }

    public void markDiscussionsRead(String groupId,int count){
        if(groupsData.containsKey(groupId)) {
            groupsData.get(groupId).put(DISCUSSION_COUNT_READ, String.valueOf(count));
            groupsData.get(groupId).put(DISCUSSION_COUNT, String.valueOf(count));
        }
    }

    public void persistData(){
        try {
            SharedPreferencesUtil.save(Constants.SHARED_STATE_GROUPS, groupsData);
            SharedPreferencesUtil.save(Constants.SHARED_STATE_TASKS, taskData);
            SharedPreferencesUtil.save(Constants.SHARED_STATE_TASKS_HISTORY, taskHistory);
        }catch (Exception ex){
            Timber.e(ex);
        }
    }

    public void reloadFromDisk(){
        HashMap<String, HashMap<String, Serializable>> data = SharedPreferencesUtil.get(Constants.SHARED_STATE_GROUPS);
        HashMap<String, String> tasks = SharedPreferencesUtil.get(Constants.SHARED_STATE_TASKS);
        HashMap<String, HashMap<String, Serializable>> tasks_history = SharedPreferencesUtil.get(Constants.SHARED_STATE_TASKS_HISTORY);
        if(data != null){
            groupsData = data;
        }

        if(tasks != null){
            taskData = tasks;
        }

        if(tasks_history != null){
            taskHistory = tasks_history;
        }
    }
}

