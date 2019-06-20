package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.services.DiscussionImpl;
import app.whatsdone.android.services.DiscussionService;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.GetCurrentDetails;
import app.whatsdone.android.utils.LocalState;
import timber.log.Timber;

public abstract class MessageActivity extends AppCompatActivity implements
        MessagesListAdapter.SelectionListener
        ,MessagesListAdapter.OnLoadMoreListener{


    protected String senderId = null;
    public Toolbar toolbar;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private DiscussionService discussionService = new DiscussionImpl();
    private GroupService groupService = new GroupServiceImpl();
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();
    public List<String> taskList = new ArrayList<>();
    public Group group;

    @Override
    public void onCreate(@Nullable Bundle persistentState) {
        super.onCreate( persistentState);
        Intent intent = getIntent();
        group = intent.getParcelableExtra(Constants.REF_TEAMS);
        taskList = intent.getStringArrayListExtra("tasks");
        imageLoader = (imageView, url, payload) -> {
            Picasso.get().load(url).placeholder(R.drawable.user_group_man_woman3x).into(imageView);
        };
        senderId = getCurrentDetails.getCurrentUser().getId();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalState.getInstance().markDiscussionsRead(group.getDocumentID(), group.getDiscussionCount());
        discussionService.subscribe(group.getDocumentID(), new ServiceListener() {
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

        groupService.subscribe(group.getDocumentID(), new ServiceListener(){
            @Override
            public void onDataReceived(BaseEntity entity) {
                group.setDiscussionCount(((Group)entity).getDiscussionCount());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalState.getInstance().markDiscussionsRead(group.getDocumentID(), group.getDiscussionCount());
        discussionService.unSubscribe();
        groupService.unSubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LocalState.getInstance().markDiscussionsRead(group.getDocumentID(), messagesAdapter.getItemCount());
        discussionService.unSubscribe();
        groupService.unSubscribe();
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
        },1000);
    }

    public Message verifyMessageInsert(Message message){
        final boolean[] insert = {false};

        boolean postDelayed = new Handler().postDelayed(() -> {

            discussionService.insertMessage(message, new ServiceListener() {
                @Override
                public void onSuccess() {
                    insert[0] = true;
                }
            });
        }, 1000);

        if (postDelayed) {
            return message;
        }

        return message;
    }



}