package app.whatsdone.android.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePolicy;
import com.otaliastudios.autocomplete.AutocompletePresenter;
import com.otaliastudios.autocomplete.CharPolicy;
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
import app.whatsdone.android.utils.User;
import app.whatsdone.android.utils.UserPresenter;

import static android.R.*;

public class InnerGroupDiscussionActivity extends MessageActivity implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener   {

    private MessagesList messagesList;
    private ArrayAdapter<String> adapter;
    private static final byte CONTENT_TYPE_VOICE = 1;
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();
    private MessageInput input;
    public int charactorCount = 0;
    public int lessCharactorCount = 0;
    private Autocomplete mentionsAutocompleteAt;
    private Autocomplete mentionsAutocompleteHash;

    //this
    public int typingStartSize = 0;
    public int typingEndSize = 0;


    public List<Integer> atLenthList = new ArrayList<>();


    @Override
    public void onCreate(Bundle persistentState) {

        super.onCreate(persistentState);
        setContentView(R.layout.activity_inner_group_discussion);


        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();
        toolbar = (Toolbar) findViewById(R.id.toolbarInChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(group.getGroupName());
        input = (MessageInput) findViewById(R.id.input_mes);
        input.setInputListener(this);
        input.setAttachmentsListener(this);

        adapter = new ArrayAdapter<String>(this, layout.simple_list_item_1, group.getMembers());
        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to task in group;
                onBackPressed();
            }
        });

        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('@');
        AutocompletePolicy policy1 = new CharPolicy('#');
        // Look for @mentions\
        AutocompletePresenter<User> presenter = new UserPresenter(this);
        AutocompleteCallback<User> callback = new AutocompleteCallback<User>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, User item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getUsername();
                editable.replace(start, end, replacement);
                // This is better done with regexes and a TextWatcher, due to what happens when
                // the user clears some parts of the text. Up to you.
                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start+replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }

            @Override
            public void onPopupVisibilityChanged(boolean shown) {

            }
        };

        mentionsAutocompleteAt = Autocomplete.<User>on(input.getInputEditText())
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();

        mentionsAutocompleteHash =Autocomplete.<User>on(input.getInputEditText())
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy1)
                .with(presenter)
                .with(callback)
                .build();

    }
//    private void changeMemberAdapter(String mess , boolean set) {
//
//        if (set) {
//            String ca = mess;
//
//            if (ca != null && !ca.isEmpty()) {
//                List<String> listFound = new ArrayList<String>();
//                for (String item : group.getMembers()) {
//
//                    if (item.contains(ca))
//                        listFound.add(item);
//                }
//
//                adapter = new ArrayAdapter<String>(this, layout.simple_list_item_1, listFound);
//                viewIn.setAdapter(adapter);
//            }
//
//        }
//    }

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
