package app.whatsdone.android.utils;

public class Constants {
    public static final boolean IS_LOGGED_IN = true;

    // User Model
    public static final String FIELD_USER_PHONE_NO = "phone_no";
    public static final String FIELD_USER_AVATAR = "avatar";
    public static final String FIELD_USER_DISPLAY_NAME = "name";
    public static final String FIELD_USER_DEVICE_TOKENS = "deviceTokens";
    public static final String FIELd_USER_STATUS = "status";
    public static final String FIELD_USER_ENABLE_NOTIFICATIONS = "enable_notifications";


    public static final String ARG_VERIFICATION_ID = "arg_verification_id";
    public static final String REF_TEAMS = "groups";
    public static final String REF_TASKS = "tasks";
    public static final String REF_USERS = "users";
    public static final String REF_DISCUSSIONS = "discussions";
    public static final String REF_CONTACTS = "contacts";

    // Group collection fields
    public static final String FIELD_GROUP_ADMINS = "admins";
    public static final String FIELD_GROUP_CREATED_BY = "created_by";
    public static final String FIELD_GROUP_DISCUSSION_COUNT = "discussion_count";
    public static final String FIELD_GROUP_ENABLE_USER_TASKS = "enable_user_tasks";
    public static final String FIELD_GROUP_MANAGED_BY_ADMIN = "managed_by_admin";
    public static final String FIELD_GROUP_MEMBERS = "members";
    public static final String FIELD_GROUP_TASKS_COUNT = "task_count";
    public static final String FIELD_GROUP_TITLE = "title";
    public static final String FIELD_GROUP_AVATAR = "avatar";
    public static final String FIELD_GROUP_UPDATED_AT = "updated_at";
    public static final String FIELD_GROUP_TASK_HISTORY = "task_history";
    public static final String FIELD_GROUP_TASK_HISTORY_UPDATED_AT = "updated_at";
    public static final String SHARED_STATE_TASKS_HISTORY = "state_task_history";
    public static final String LISTENER_GROUP_ALL = "group_all_listener";
    public static final String LISTENER_MY_TASK = "my_task_listener";
    public static final String FIELD_GROUP_TASK_HISTORY_UPDATED_BY = "updated_by";

    // Task Fields
    public static int TASKS_LIMIT = 50;
    public static final String FIELD_TASK_ASSIGNED_USER = "assigned_user";
    public static final String FIELD_TASK_ASSIGNED_BY = "assigned_by";
    public static final String FIELD_TASK_ASSIGNED_BY_NAME = "assigned_by_name";
    public static final String FIELD_TASK_ASSIGNED_USER_IMAGE = "assigned_user_image";
    public static final String FIELD_TASK_DUE_AT = "due_at";
    public static final String FIELD_TASK_ASSIGNED_USER_NAME = "assigned_user_name";
    public static final String FIELD_TASK_ASSIGNEE_COMMENT = "assignee_comment";
    public static final String FIELD_TASK_CREATED_BY = "created_by";
    public static final String FIELD_TASK_CREATED_BY_NAME = "created_by_name";
    public static final String FIELD_TASK_CREATED_AT = "created_at";
    public static final String FIELD_TASK_DESCRIPTION = "description";
    public static final String FIELD_TASK_STATUS = "status";
    public static final String FIELD_TASK_GROUP_ID = "group_id";
    public static final String FIELD_TASK_GROUP_NAME = "group_name";
    public static final String FIELD_TASK_TITLE = "title";
    public static final String FIELD_TASK_UPDATED_AT = "updated_at";
    public static final String FIELD_TASK_CHECKLIST = "checklist";
    public static final String FIELD_TASK_CHECKLIST_TITLE = "title";
    public static final String FIELD_TASK_CHECKLIST_COMPLETED = "is_completed";
    public static final String ACKNOWLEDGED = "is_acknowledged";



    // Discussion Field
    public static final String FIELD_DISCUSSION_USER_ID = "by_user";
    public static final String FIELD_DISCUSSION_USER_NAME = "user_name";
    public static final String FIELD_DISCUSSION_GROUP_ID = "group_id";
    public static final String FIELD_DISCUSSION_MESSAGE = "message";
    public static final String FIELD_DISCUSSION_POSTED_AT = "posted_at";
    public static final String FIELD_DISCUSSION_USER_AVATAR = "user_image";


    public static final String ARG_GROUP_ID = "group_id";
    public static final String ARG_GROUP_NAME = "group_title";

    public static final String URL_FIREBASE = "https://us-central1-whatsdone-f770e.cloudfunctions.net/api/";
    public static final String URL_FIREBASE_STORAGE = "https://firebasestorage.googleapis.com/v0/b/whatsdone-f770e.appspot.com/o/";

    public static final int IMAGE_HEIGHT = 180;
    public static final int IMAGE_WIDTH = 180;

    public static final String SHARED_TOKEN = "token";
    public static final String DEFULT_CHAT_IMG_URL = "https://img.icons8.com/color/100/000000/user-group-man-man.png";
    public static final String SHARED_PHONE = "phone_no";

    public static final String ACTION_ADD_GROUP = "add_group";
    public static final String ACTION_VIEW_GROUP = "view_group";
    public static final String ARG_GROUP = "group";
    public static final String ARG_ACTION = "action";
    public static final String FIELD_USER_ACTIVE = "activated_status";
    public static final String ACTION_ADD_TASK = "add_task";
    public static final String GROUP_PERSONAL = "Personal";
    public static final String ACTION_VIEW_TASK = "view_task";
    public static final String ARG_TASK = "task";
    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String DATETIME_FORMAT = "MMM dd yyyy HH:mm:ss.SSS zzz";
    public static long DISCUSSION_LIMIT = 50;
    public static final String FIELD_GROUP_MEMBERS_DETAILS = "member_details";


    public static final String FIELD_GROUP_MEMBERS_DETAILS_PHONE = "phone_no";
    public static final String FIELD_GROUP_MEMBERS_DETAILS_INVITED = "is_invited";
    public static final String FIELD_GROUP_MEMBERS_DETAILS_NAME = "display_name";
    public static final String SHARED_STATE_GROUPS = "group_state";
    public static final String DISABLE_NOTIFICATION = "disable_notifications";
    public static final String SHARED_STATE_TASKS = "task_state";
    public static final String ARG_TYPE = "type";
    public static final String NOTIFICATION_TO_ME = "assigned you";
    public static final String ARG_CLICK_ACTION = "click_action";
    public static final String ACTION_GROUP_ACTIVITY = "GROUP_ACTIVITY";
    public static final String REF_LOGS = "activity_log";
    public static final String FIELD_LOG_GROUP_ID = "group_id";
    public static final String FIELD_LOG_LOGS = "logs";
    public static final String FIELD_LOG_LOGS_BY_USER = "by_user";
    public static final String FIELD_LOG_LOGS_BY_USERNAME = "by_user_name";
    public static final String FIELD_LOG_LOGS_VALUE_FROM = "value_from";
    public static final String FIELD_LOG_LOGS_VALUE_TO = "value_to";
    public static final String FIELD_LOG_LOGS_DATE = "ts";
    public static final String FIELD_LOG_LOGS_TYPE = "type";
    public static final String INVITED = "Invited";
    public static final String FIELD_TASK_UPDATED_BY = "updated_by";
    public static final String CONFIG_TASK_LIMIT = "task_limit";
    public static final String CONFIG_MESSAGE_LIMIT = "discussion_limit";
}
