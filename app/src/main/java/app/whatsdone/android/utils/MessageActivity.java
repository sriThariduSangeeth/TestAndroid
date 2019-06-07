package app.whatsdone.android.utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.services.DiscussionImpl;
import app.whatsdone.android.services.DiscussionService;
import app.whatsdone.android.services.ServiceListener;

public abstract class MessageActivity extends AppCompatActivity implements
        MessagesListAdapter.SelectionListener
        ,MessagesListAdapter.OnLoadMoreListener{


    protected String senderId = null;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;
    private String id = "I0W3Nrrr0IpUVaI33SnU";
    private List<Message> allMessages = new ArrayList<>();
    private DiscussionService discussionService = new DiscussionImpl();
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();

    @Override
    public void onCreate(@Nullable Bundle persistentState) {
        super.onCreate( persistentState);
        imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);
        senderId = getCurrentDetails.getCurrentUser().getId();
    }

    @Override
    protected void onStart() {

        super.onStart();
        allMessages.clear();
        discussionService.getAllDiscussion(id, new ServiceListener() {
            @Override
            public void onDataReceivedForMessage(ArrayList<Message> messages) {
                if(!messages.isEmpty()){
                    allMessages.addAll(messages);
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

        Message lastMes = allMessages.get(page-1);

        new Handler().postDelayed(() -> {
            discussionService.loadRestMessages(lastMes, id , new ServiceListener() {
                @Override
                public void onDataReceivedForMessage(ArrayList<Message> messages) {
                    messagesAdapter.addToEnd( messages, false);
                    allMessages.addAll(messages);

                }
            });
        },1000);
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return message -> {
            String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                    .format(message.getCreatedAt());

            String text = message.getText();
            if (text == null) text = "[attachment]";

            return String.format(Locale.getDefault(), "%s: %s (%s)",
                    message.getUser().getName(), text, createdAt);
        };
    }


    public Message verifyMessageInsert(Message message){
        final boolean[] insert = {false};

        new Handler().postDelayed(() -> {

            discussionService.insterMessage(message, new ServiceListener() {
                @Override
                public void onSuccess() {
                    insert[0] = true;
                }
            });
        },1000);

        if (insert[0]) return message;

        return message;
    }

}