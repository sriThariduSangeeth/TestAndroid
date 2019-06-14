package app.whatsdone.android.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;

public class LocalState {
    private static final String TASK_COUNT = "task_count";
    private static final String TASK_COUNT_READ = "task_count_read";
    private static final String DISCUSSION_COUNT = "discussion_count";
    private static final String DISCUSSION_COUNT_READ = "discussion_count_read";
    private static final String TASK_UPDATED_AT = "task_updated_at";
    private static final String DISCUSSION_UPDATED_AT = "discussion_updated_at";
    private static final String FORMAT = "MMM dd yyyy HH:mm:ss.SSS zzz";
    private LocalState() {}

    private static class LazyHolder {
        static final LocalState INSTANCE = new LocalState();
    }
    public static LocalState getInstance() {
        return LazyHolder.INSTANCE;
    }

    private HashMap<String, HashMap<String, Serializable>> groupsData = new HashMap<>();

    public void syncGroups(List<BaseEntity> groups){
        for (BaseEntity entity: groups) {
            Group group = (Group)entity;
            int totalTaskCount = group.getTaskCount();
            int totalDiscussionCount = group.getDiscussionCount();
            Date updated = group.getUpdatedDate();
            String id =  group.getDocumentID();
            if(groupsData.containsKey(id)){
                HashMap data = groupsData.get(id);
                int taskCount = (int) data.get(TASK_COUNT_READ);
                int discussionCount = (int) data.get(DISCUSSION_COUNT_READ);
                Date taskUpdated = DateUtil.parse((String) data.get(TASK_UPDATED_AT), FORMAT);
                Date discussionUpdated = DateUtil.parse((String) data.get(DISCUSSION_UPDATED_AT), FORMAT);

                groupsData.get(id).put(DISCUSSION_COUNT, totalDiscussionCount);
                group.setUnreadDiscussionCount(totalDiscussionCount - discussionCount);

                groupsData.get(id).put(TASK_COUNT, totalTaskCount);
                group.setUnreadTaskCount(totalTaskCount - taskCount);



            }else {
                HashMap<String, Serializable> data = new HashMap<>();
                data.put(TASK_COUNT, totalTaskCount);
                data.put(TASK_COUNT_READ, 0);
                data.put(DISCUSSION_COUNT, totalDiscussionCount);
                data.put(DISCUSSION_COUNT_READ, 0);
                data.put(DISCUSSION_UPDATED_AT, DateUtil.formatted(updated, FORMAT));
                data.put(TASK_UPDATED_AT, DateUtil.formatted(updated, FORMAT));
                groupsData.put(id, data);
                group.setUnreadDiscussionCount(totalDiscussionCount);
                group.setUnreadTaskCount(totalTaskCount);
            }
        }
    }

    public void markTasksRead(String groupId, int count){
        if(groupsData.containsKey(groupId)) {
            groupsData.get(groupId).put(TASK_COUNT_READ, count);
            groupsData.get(groupId).put(TASK_COUNT, count);
        }
    }

    public void markDiscussionsRead(String groupId,int count){
        if(groupsData.containsKey(groupId)) {
            groupsData.get(groupId).put(DISCUSSION_COUNT_READ, count);
            groupsData.get(groupId).put(DISCUSSION_COUNT, count);
        }
    }
}
