package app.whatsdone.android.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.utils.GetCurrentDetails;
import app.whatsdone.android.utils.MessageActivity;

import static android.R.*;

public class InnerGroupDiscussionActivity extends MessageActivity implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener  {

    private MessagesList messagesList;
    private ArrayAdapter<String> adapter;
    private static final byte CONTENT_TYPE_VOICE = 1;
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();
    private MessageInput input;
    private ListView viewIn;
    public boolean hitAt = false;
    public boolean hitHash = false;
    public boolean hitSpace = false;
    public int charactorCount = 0;
    public int lessCharactorCount = 0;

    //this
    public int typingStartSize = 0;
    public int typingEndSize = 0;


    public List<Integer> atLenthList = new ArrayList<>();


    @Override
    public void onCreate(Bundle persistentState) {

        super.onCreate(persistentState);
        setContentView(R.layout.activity_inner_group_discussion);


        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.viewIn = (ListView) findViewById(R.id.view_in);
        initAdapter();
        toolbar = (Toolbar) findViewById(R.id.toolbarInChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(group.getGroupName());
        input = (MessageInput) findViewById(R.id.input_mes);
        input.setInputListener(this);
        input.setAttachmentsListener(this);

        adapter = new ArrayAdapter<String>(this, layout.simple_list_item_1, group.getMembers());
        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        viewIn.setAdapter(adapter);
        viewIn.setVisibility(View.GONE);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to task in group;
                onBackPressed();
            }
        });

        input.getInputEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("before text change : "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("change text : "+s);

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("after text change : "+s);

            }
        });


        input.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        catchAt();
//                    }
//                }, 1000);
//                typingStartSize = input.getInputEditText().getText().toString().length();

            }
            @Override
            public void onStopTyping() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        catchAt();
                    }
                }, 1000);
//                typingEndSize = input.getInputEditText().getText().toString().length();
//                if(typingStartSize > typingEndSize){
//                    String checkword = input.getInputEditText().getText().toString().substring(typingEndSize-1);
//                    if(hitAt && checkword.equals("@")){
//                        viewIn.setVisibility(View.VISIBLE);
//                    }
//                }else if(typingStartSize <= typingEndSize ){
//                    //when insert characters
//                    String checkword = input.getInputEditText().getText().toString().substring(typingStartSize-1);
//                    if(checkword.indexOf("@") > -1 && !hitAt){
//                        hitAt = true;
//                        hitSpace = false;
//                        atLenthList.add(typingEndSize);
//                        viewIn.setVisibility(View.VISIBLE);
//                        System.out.println("@ catch");
//                    }else if(checkword.indexOf("#") > -1 && !hitHash){
//                        hitHash = true;
//                        hitSpace = false;
//                        System.out.println("# catch");
//                    }else if(checkword.indexOf(" ") > -1 && (hitAt || hitHash) ){
//                        viewIn.setVisibility(View.GONE);
//                        hitSpace = true;
//                        hitAt = false;
//                        hitHash = false;
//                    }
//                    if(hitAt && !checkword.equals("@")){
//                        changeMemberAdapter(checkword, true);
//                    }else if (hitAt && checkword.equals("@")) {
//                        viewIn.setVisibility(View.VISIBLE);
//                    }
//                }

            }
        });

        viewIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringBuilder stringBuilder = new StringBuilder();
                String selc = ((AppCompatTextView) view).getText().toString();
                if(selc != null){
                    stringBuilder.append(selc+" ");
                }
                input.getInputEditText().setText(input.getInputEditText().getText().toString()+stringBuilder);
                viewIn.setVisibility(View.GONE);
                hitSpace = false;
                hitAt= false;
                Selection.setSelection(input.getInputEditText().getText(),input.getInputEditText().length());
                lessCharactorCount = input.getInputEditText().getText().length();

            }
        });

    }


    private void catchAt() {

        boolean set = false;
        String inputText = input.getInputEditText().getText().toString();
        charactorCount = inputText.length();

        try {
            if(charactorCount > lessCharactorCount){
                if (!hitAt) {
                    if (inputText.substring(lessCharactorCount).indexOf("@") > -1) {
                        viewIn.setVisibility(View.VISIBLE);
                        adapter = new ArrayAdapter<String>(this, layout.simple_list_item_1, group.getMembers());
                        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
                        viewIn.setAdapter(adapter);
                        hitSpace = false;
                        lessCharactorCount = charactorCount;
                        atLenthList.add(inputText.length());
                        hitAt = true;
                    }
                } else if (inputText.length() < lessCharactorCount) {
                    viewIn.setVisibility(View.GONE);
                    atLenthList.remove(atLenthList.size() - 1);
                    lessCharactorCount = atLenthList.get(atLenthList.size() - 1);
                } else {
                    if (!hitSpace && inputText.substring(lessCharactorCount).indexOf(" ") > -1 && inputText.length() > lessCharactorCount) {
                        //after @ first space and break.
                        hitAt = false;
                        hitSpace = true;
                        viewIn.setVisibility(View.GONE);
                        lessCharactorCount = inputText.length();
                    } else {
                        changeMemberAdapter(inputText.substring(lessCharactorCount),true);
                    }

                }
            }else {
                lessCharactorCount--;
                if (!atLenthList.isEmpty()) {
                    if (inputText.length() == atLenthList.get(atLenthList.size() - 1) && !hitAt) {
                        viewIn.setVisibility(View.VISIBLE);
                        hitAt = true;
                        hitSpace = false;
                        atLenthList.remove(atLenthList.size() - 1);
                        if (atLenthList.isEmpty()){lessCharactorCount = 0;}
                        lessCharactorCount = atLenthList.get(atLenthList.size() - 1);
                    }
                }
            }


        }catch(
                Exception e)

        {
            Log.d("ERROR", e.getMessage());
        }

    }


    private void changeMemberAdapter(String mess , boolean set) {

        if (set) {
            String ca = mess;

            if (ca != null && !ca.isEmpty()) {
                List<String> listFound = new ArrayList<String>();
                for (String item : group.getMembers()) {

                    if (item.contains(ca))
                        listFound.add(item);
                }

                adapter = new ArrayAdapter<String>(this, layout.simple_list_item_1, listFound);
                viewIn.setAdapter(adapter);
            }

        }
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
        message.setId(group.getDocumentID());
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
