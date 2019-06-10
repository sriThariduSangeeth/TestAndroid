package app.whatsdone.android.utils;

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

import java.util.ArrayList;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.services.DiscussionImpl;
import app.whatsdone.android.services.DiscussionService;
import app.whatsdone.android.services.ServiceListener;

public abstract class MessageActivity extends AppCompatActivity implements
        MessagesListAdapter.SelectionListener
        ,MessagesListAdapter.OnLoadMoreListener{


    protected String senderId = null;
    public Toolbar toolbar;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private DiscussionService discussionService = new DiscussionImpl();
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();
    public Group group;

    @Override
    public void onCreate(@Nullable Bundle persistentState) {
        Intent intent = getIntent();
        group = intent.getParcelableExtra(Constants.REF_TEAMS);

        super.onCreate( persistentState);
        imageLoader = (imageView, url, payload) -> {
            Picasso.get().load(url).into(imageView);
        };
        senderId = getCurrentDetails.getCurrentUser().getId();

    }

    @Override
    protected void onStart() {

        super.onStart();
        discussionService.getAllDiscussion(group.getDocumentID(), new ServiceListener() {
            @Override
            public void onDataReceivedForMessage(ArrayList<Message> messages) {
                if(!messages.isEmpty()){
                    messagesAdapter.addToEnd( messages, false);
                }else {
                    Log.d("TAG", "There is no any messages");
                }
            }
        });
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);

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