package app.whatsdone.android.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.utils.GetCurrentDetails;
import app.whatsdone.android.utils.MessageActivity;

public class InnerGroupDiscussionActivity extends MessageActivity  implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener{

//    private static String id = "";
    private MessagesList messagesList;
    private static final byte CONTENT_TYPE_VOICE = 1;
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();
    private MessageInput input;
    private ListView viewIn;


    @Override
    public void onCreate(Bundle persistentState) {

        String[] users = { "Suresh Dasari", "Trishika Dasari", "Rohini Alavala", "Praveen Kumar", "Madhav Sai" };


        super.onCreate(persistentState);
        setContentView(R.layout.activity_inner_group_discussion);
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.viewIn = (ListView) findViewById(R.id.view_in);
        initAdapter();
        toolbar = (Toolbar) findViewById(R.id.toolbarInChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupName);
        input = (MessageInput) findViewById(R.id.input_mes);
        input.setInputListener(this);
        input.setAttachmentsListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to task in group;
                onBackPressed();
            }
        });

        input.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {
                Log.d("Tag", String.valueOf(input.getInputEditText().getText()));
                if(input.getInputEditText().getText().toString().indexOf("@") > -1){
                    viewIn.setAdapter(adapter);
                    int num = input.getInputEditText().getText().toString().indexOf("@");
                }


            }

            @Override
            public void onStopTyping() {

            }
        });

    }


    public String getGroupId (){
        return id;
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
        Message message = new Message();
        message.setId(id);
        message.setCreatedAt(getCurrentDetails.getCurrentDateTime());
        message.setText(input.toString());
        message.setUser(getCurrentDetails.getCurrentUser());

        super.messagesAdapter.addToStart(verifyMessageInsert(message), true);
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
