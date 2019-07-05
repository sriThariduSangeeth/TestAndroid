package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.model.MessageFormatter;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.DiscussionImpl;
import app.whatsdone.android.services.DiscussionService;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.GetCurrentDetails;
import app.whatsdone.android.utils.LocalState;
import timber.log.Timber;

import static app.whatsdone.android.utils.Constants.ARG_TASK;

public abstract class MessageActivity extends AppCompatActivity implements
        MessagesListAdapter.SelectionListener
        ,MessagesListAdapter.OnLoadMoreListener{


    protected String senderId = null;
    public Toolbar toolbar;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private DiscussionService discussionService = new DiscussionImpl();
    private GroupService groupService = GroupServiceImpl.getInstance();
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();
    public List<Task> taskList = new ArrayList<>();
    public Group group;

    MessageFormatter formatter = new MessageFormatter() {
        @Override
        public String formatMessage(String text) {
            boolean numbersInText = text.contains("@");
            boolean tasksInText = text.contains("#");
            String textMessage = text;
            if(numbersInText || tasksInText){
                String[] words = text.split(" ");
                for (String word : words) {
                    if (word.startsWith("@")) {
                        String phone = word.substring(1);
                        String name = ContactUtil.getInstance().resolveContact(phone, group.getMemberDetails()).getDisplayName();
                        textMessage = textMessage.replace(phone, name);
                    }

                    if (word.startsWith("#")) {
                        String taskId = word.substring(1);
                        for (Task task : taskList) {
                            if(task.getDocumentID().equals(taskId)){
                                textMessage = textMessage.replace(taskId, task.getTitle());
                            }
                        }

                    }
                }
            }
            return textMessage;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle persistentState) {
        super.onCreate( persistentState);
        Intent intent = getIntent();
        group = intent.getParcelableExtra(Constants.ARG_GROUP);
        Object[] data = (Object[]) intent.getExtras().get(ARG_TASK);
        for (Object datum : data) {
            taskList.add((Task) datum);
        }
        imageLoader = (ImageView imageView, String url, Object payload) -> {
            Picasso.get().load(url).placeholder(R.drawable.user_group_man_woman3x).into(imageView);
        };
        senderId = getCurrentDetails.getCurrentUser().getId();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalState.getInstance().markDiscussionsRead(group.getDocumentID(), group.getDiscussionCount());
        discussionService.subscribe(group.getDocumentID(), formatter, new ServiceListener() {
            @Override
            public void onDataReceivedForMessage(ArrayList<Message> messages) {
                if(!messages.isEmpty()){
                    messagesAdapter.clear();
                    messagesAdapter.addToEnd( messages, false);
                }else {
                    Timber.d("There is no any messages");
                }
            }
        });

        groupService.subscribeForGroup(group.getDocumentID(), new ServiceListener(){
            @Override
            public void onDataReceived(BaseEntity entity) {
                group.setDiscussionCount(((Group)entity).getDiscussionCount());
                LocalState.getInstance().markDiscussionsRead(group.getDocumentID(), group.getDiscussionCount());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalState.getInstance().markDiscussionsRead(group.getDocumentID(), group.getDiscussionCount());
        discussionService.unSubscribe();
        groupService.removeListener(group.getDocumentID());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        discussionService.unSubscribe();
        groupService.removeListener(group.getDocumentID());
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Timber.i("onLoadMore: " + page + " " + totalItemsCount);

        new Handler().postDelayed(() -> {
            discussionService.loadRestMessages( group.getDocumentID() , new ServiceListener() {
                @Override
                public void onDataReceivedForMessage(ArrayList<Message> messages) {
                    messagesAdapter.addToEnd( messages, false);
                }
            });
        },0);
    }

    public Message verifyMessageInsert(Message message){

        boolean postDelayed = new Handler().postDelayed(() -> {
            String text = message.getText();

           for(int i=0; i<group.getMemberDetails().size() ; i++) {
                Contact member = ContactUtil.getInstance().resolveContact(group.getMemberDetails().get(i).getPhoneNumber(), group.getMemberDetails());
               if(text.contains(member.getDisplayName())) {
                       text = text.replace("@"+member.getDisplayName(), "@"+group.getMemberDetails().get(i).getPhoneNumber());
                   }
           }

           
           message.setText(text);

            discussionService.insertMessage(message, new ServiceListener() {
                @Override
                public void onSuccess() {
                    Timber.d("%s saved", message);
                }
            });
        }, 0);

        if (postDelayed) {
            return message;
        }

        return message;
    }



}