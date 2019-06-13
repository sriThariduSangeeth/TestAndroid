package app.whatsdone.android.ui.activity;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ArrayAdapter;


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

import app.whatsdone.android.R;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.GetCurrentDetails;
import app.whatsdone.android.utils.MessageActivity;
import app.whatsdone.android.utils.UserPresenter;

import static android.R.*;

public class InnerGroupDiscussionActivity extends MessageActivity implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener   {

    private MessagesList messagesList;
    private ArrayAdapter<String> adapter;
    private List<Contact> contactList;
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();
    private MessageInput input;
    private Autocomplete mentionsAutocompleteAt;
    private Autocomplete mentionsAutocompleteHash;



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

        contactList = ContactUtil.getInstance().resolveContacts(group.getMembers() , group.getMemberDetails());

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
        AutocompletePresenter<Contact> presenter = new UserPresenter(this , contactList);
        AutocompleteCallback<Contact> callback = new AutocompleteCallback<Contact>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, Contact item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getDisplayName();
                editable.replace(start, end, replacement);
                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start+replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }

            @Override
            public void onPopupVisibilityChanged(boolean shown) {

            }
        };

        mentionsAutocompleteAt = Autocomplete.<Contact>on(input.getInputEditText())
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();

        mentionsAutocompleteHash =Autocomplete.<Contact>on(input.getInputEditText())
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
