package app.whatsdone.android.ui.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import app.whatsdone.android.R;
import app.whatsdone.android.fixtures.MessagesFixtures;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.utils.MessageActivity;

public class InnerGroupDiscussionActivity extends MessageActivity  implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener {

    private Toolbar toolbar;
    private MessagesList messagesList;
    private static final byte CONTENT_TYPE_VOICE = 1;


    @Override
    public void onCreate(Bundle persistentState) {
        super.onCreate(persistentState);
        setContentView(R.layout.activity_inner_group_discussion);
        toolbar = (Toolbar) findViewById(R.id.toolbarInChat);
        setSupportActionBar(toolbar);
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();

        MessageInput input = (MessageInput) findViewById(R.id.input_mes);
        input.setInputListener(this);
        input.setAttachmentsListener(this);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to task in group;
                onBackPressed();
            }
        });
    }


    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onSelectionChanged(int count) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        return false;
    }

    @Override
    public void onAddAttachments() {

    }

    //send Massage
    @Override
    public boolean onSubmit(CharSequence input) {
        super.messagesAdapter.addToStart(MessagesFixtures.getTextMessage(input.toString()), true);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initAdapter() {
        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        this.messagesList.setAdapter(super.messagesAdapter);
    }

}
