package app.whatsdone.android.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.whatsdone.android.model.Group;

public class LocalState {
    private static final String TASK_COUNT = "task_count";
    private static final String DISCUSSION_COUNT = "discussion_count";
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

    public void syncGroups(List<Group> groups){
        for (Group group : groups) {
            int totalTaskCount = group.getTaskCount();
            int totalDiscussionCount = group.getDiscussionCount();
            String id =  group.getDocumentID();
            if(groupsData.containsKey(id)){
                HashMap data = groupsData.get(id);
                int taskCount = (int) data.get(TASK_COUNT);
                int discussionCount = (int) data.get(DISCUSSION_COUNT);
                Date taskUpdated = DateUtil.parse((String) data.get(TASK_UPDATED_AT), FORMAT);
                Date discussionUpdated = DateUtil.parse((String) data.get(DISCUSSION_UPDATED_AT), FORMAT);


                if(taskUpdated.before(group.getUpdatedDate())){
                    groupsData.get(id).put(DISCUSSION_UPDATED_AT, group.getUpdatedDate());
                    groupsData.get(id).put(TASK_COUNT, group.getTaskCount());
                    group.setUnreadTaskCount(totalTaskCount - taskCount);
                }else {
                    group.setUnreadTaskCount(0);
                }

                if(discussionUpdated.before(group.getUpdatedDate())){
                    groupsData.get(id).put(TASK_UPDATED_AT, group.getUpdatedDate());
                    groupsData.get(id).put(DISCUSSION_COUNT, group.getDiscussionCount());
                    group.setUnreadDiscussionCount(totalDiscussionCount - discussionCount);
                }else {
                    group.setUnreadTaskCount(0);
                }


            }else {
                HashMap<String, Serializable> data = new HashMap<>();
                data.put(TASK_COUNT, group.getTaskCount());
                data.put(DISCUSSION_COUNT, totalTaskCount);
                data.put(DISCUSSION_UPDATED_AT, DateUtil.formatted(group.getUpdatedDate(), FORMAT));
                data.put(TASK_UPDATED_AT, DateUtil.formatted(group.getUpdatedDate(), FORMAT));
                groupsData.put(id, data);
                group.setUnreadDiscussionCount(totalDiscussionCount);
                group.setUnreadTaskCount(totalTaskCount);
            }
        }
    }

    public void markTasksRead(String groupId){
        if(groupsData.containsKey(groupId))
            groupsData.get(groupId).put(TASK_UPDATED_AT,DateUtil.formatted(new Date(), FORMAT));
    }

    public void markDiscussionsRead(String groupId){
        if(groupsData.containsKey(groupId))
            groupsData.get(groupId).put(DISCUSSION_UPDATED_AT,DateUtil.formatted(new Date(), FORMAT));
    }
}
