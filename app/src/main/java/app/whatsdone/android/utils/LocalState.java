package app.whatsdone.android.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthServiceImpl;
import timber.log.Timber;

import static app.whatsdone.android.utils.Constants.DATETIME_FORMAT;
import static app.whatsdone.android.utils.Constants.FIELD_GROUP_TASK_HISTORY_UPDATED_AT;
import static app.whatsdone.android.utils.Constants.FIELD_GROUP_TASK_HISTORY_UPDATED_BY;

public class LocalState {
    private static final String TASK_COUNT = "task_count";
    private static final String UPDATED_AT = "updated_at";
    private static final String DISCUSSION_COUNT = "discussion_count";
    private static final String DISCUSSION_COUNT_READ = "discussion_count_read";

    private LocalState() {
    }

    private static class LazyHolder {
        static final LocalState INSTANCE = new LocalState();
    }

    public static LocalState getInstance() {
        return LazyHolder.INSTANCE;
    }

    private HashMap<String, HashMap<String, Serializable>> groupsData = new HashMap<>();
    private HashMap<String, HashMap<String, Serializable>> taskHistory = new HashMap<>();

    public void syncTasks(List<BaseEntity> tasks) {
        for (BaseEntity entity : tasks) {
            Task task = (Task) entity;
            String current = AuthServiceImpl.getCurrentUser().getDocumentID();

            if (taskHistory.containsKey(task.getGroupId())) {
                HashMap<String, Serializable> taskData = taskHistory.get(task.getGroupId());
                if (taskData.containsKey(task.getDocumentID())) {
                    Date taskUpdatedDate = task.getUpdatedDate();
                    Date localTaskUpdated = DateUtil.parse(taskData.get(task.getDocumentID()).toString(), DATETIME_FORMAT);
                    Timber.d("task updated: %s, local: %s", taskUpdatedDate, localTaskUpdated);
                    if (!DateUtil.isDateTimeEqual(taskUpdatedDate, localTaskUpdated) && localTaskUpdated.before(taskUpdatedDate)) {
                        if(!task.getUpdatedBy().equals(current)) { task.setUnreadTask(true);}
                    } else if (DateUtil.isDateTimeEqual(taskUpdatedDate, localTaskUpdated)) {
                        if(!task.getUpdatedBy().equals(current)) { task.setUnreadTask(true);}
                    } else{
                        task.setUnreadTask(false);
                    }
                }else {
                    if(!task.getUpdatedBy().equals(current)) { task.setUnreadTask(true);}
                    taskHistory.get(task.getGroupId()).put(task.getDocumentID(), DateUtil.formatted(task.getUpdatedDate(), DATETIME_FORMAT));
                }
            } else {
                if(!task.getUpdatedBy().equals(current)) { task.setUnreadTask(true);}
            }
            Timber.d("%s is unread: %b", task.getTitle(), task.isUnreadTask());
        }

    }

    public void setTaskRead(Task task) {
        String now = DateUtil.formatted(DateUtil.addTime(task.getUpdatedDate(), 2), DATETIME_FORMAT);
        if(groupsData.containsKey(task.getGroupId())){
            groupsData.get(task.getGroupId()).put(UPDATED_AT, now);
        }
        if (taskHistory.containsKey(task.getGroupId())) {
            taskHistory.get(task.getGroupId()).put(task.getDocumentID(),now );
        }
        Timber.d("%s is marked as read", task.getTitle());
    }

    public void syncGroups(List<BaseEntity> groups) {
        boolean isFirstTime = false;
        String currentUser = AuthServiceImpl.getCurrentUser().getDocumentID();
        if (groupsData.isEmpty())
            isFirstTime = true;
        for (BaseEntity entity : groups) {
            Group group = (Group) entity;
            int totalTaskCount = group.getTaskCount();
            int totalDiscussionCount = group.getDiscussionCount();
            Date updated = group.getUpdatedDate();
            String id = group.getDocumentID();
            if (groupsData.containsKey(id)) {
                HashMap data = groupsData.get(id);
                int discussionCount = Integer.parseInt((String) data.get(DISCUSSION_COUNT_READ));
                Date updatedAt = DateUtil.parse((String) data.get(UPDATED_AT), DATETIME_FORMAT);

                groupsData.get(id).put(DISCUSSION_COUNT, String.valueOf(totalDiscussionCount));
                group.setUnreadDiscussionCount(Math.max(totalDiscussionCount - discussionCount, 0));

                if (DateUtil.isDateTimeEqual(updated, updatedAt)) {
                    int totalCount = Integer.parseInt((String) data.get(TASK_COUNT));
                    group.setUnreadTaskCount(totalCount);

                } else {
                    groupsData.get(id).put(UPDATED_AT, DateUtil.formatted(updated, DATETIME_FORMAT));
                    int unread = 0;
                    if (taskHistory.containsKey(id)) {
                        HashMap taskData = taskHistory.get(id);
                        for (String taskId : group.getTaskDetails().keySet()) {
                            Date taskUpdatedDate = (Date) group.getTaskDetails().get(taskId).get(FIELD_GROUP_TASK_HISTORY_UPDATED_AT);
                            String updatedBy = (String) group.getTaskDetails().get(taskId).get(FIELD_GROUP_TASK_HISTORY_UPDATED_BY);

                                if (taskData.containsKey(taskId)) {
                                    Date localTaskUpdated = DateUtil.parse(taskData.get(taskId).toString(), DATETIME_FORMAT);
                                    if (!DateUtil.isDateTimeEqual(taskUpdatedDate, localTaskUpdated) && localTaskUpdated.before(taskUpdatedDate)) {
                                        if(!currentUser.equals(updatedBy)) unread++;
                                        taskHistory.get(id).put(taskId, DateUtil.formatted(taskUpdatedDate, DATETIME_FORMAT));
                                    } else if (DateUtil.isDateTimeEqual(taskUpdatedDate, localTaskUpdated)) {
                                        if(!currentUser.equals(updatedBy)) unread++;
                                    }
                                } else {
                                    if(!currentUser.equals(updatedBy)) unread++;
                                    taskHistory.get(id).put(taskId, DateUtil.formatted(taskUpdatedDate, DATETIME_FORMAT));
                                }
                        }
                    } else {
                        HashMap<String, Serializable> taskData = new HashMap<>();
                        for (String taskId : group.getTaskDetails().keySet()) {
                            Date taskUpdated = (Date) group.getTaskDetails().get(taskId).get(FIELD_GROUP_TASK_HISTORY_UPDATED_AT);
                            taskData.put(taskId, DateUtil.formatted(taskUpdated, DATETIME_FORMAT));
                        }
                        taskHistory.put(id, taskData);
                        unread = taskData.size();
                    }

                    groupsData.get(id).put(TASK_COUNT, String.valueOf(unread));
                    group.setUnreadTaskCount(unread);
                }

            } else {
                HashMap<String, Serializable> data = new HashMap<>();
                data.put(DISCUSSION_COUNT, String.valueOf(totalDiscussionCount));
                data.put(DISCUSSION_COUNT_READ, isFirstTime ? String.valueOf(totalDiscussionCount) : String.valueOf(0));
                group.setUnreadDiscussionCount(isFirstTime ? 0 : totalDiscussionCount);
                group.setUnreadTaskCount(isFirstTime ? 0 : totalTaskCount);
                data.put(UPDATED_AT, DateUtil.formatted(group.getUpdatedDate(), DATETIME_FORMAT));
                data.put(TASK_COUNT, String.valueOf(totalTaskCount));

                updateTaskHistory(group, id);
                groupsData.put(id, data);
            }
        }
    }

    private void updateTaskHistory(Group group, String id) {
        HashMap<String, Serializable> taskData = new HashMap<>();
        for (String taskId : group.getTaskDetails().keySet()) {
            Date taskUpdated = (Date) group.getTaskDetails().get(taskId).get(UPDATED_AT);
            taskData.put(taskId, DateUtil.formatted(taskUpdated, DATETIME_FORMAT));
        }
        taskHistory.put(id, taskData);
    }


    public void markDiscussionsRead(String groupId, int count) {
        if (groupsData.containsKey(groupId)) {
            groupsData.get(groupId).put(DISCUSSION_COUNT_READ, String.valueOf(count));
            groupsData.get(groupId).put(DISCUSSION_COUNT, String.valueOf(count));
        }
    }

    public void persistData() {
        try {
            SharedPreferencesUtil.save(Constants.SHARED_STATE_GROUPS, groupsData);
            SharedPreferencesUtil.save(Constants.SHARED_STATE_TASKS_HISTORY, taskHistory);
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }

    public void reloadFromDisk() {
        HashMap<String, HashMap<String, Serializable>> data = SharedPreferencesUtil.get(Constants.SHARED_STATE_GROUPS);
        HashMap<String, HashMap<String, Serializable>> tasks_history = SharedPreferencesUtil.get(Constants.SHARED_STATE_TASKS_HISTORY);
        if (data != null) {
            groupsData = data;
        }

        if (tasks_history != null) {
            taskHistory = tasks_history;
        }
    }
}

