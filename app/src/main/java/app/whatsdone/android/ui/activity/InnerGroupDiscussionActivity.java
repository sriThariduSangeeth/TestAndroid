package app.whatsdone.android.ui.activity;


import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.StyleSpan;
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

import java.util.List;
import java.util.Objects;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.ui.adapters.TaskPresenter;
import app.whatsdone.android.ui.adapters.UserPresenter;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.GetCurrentDetails;

import static android.R.layout;

public class InnerGroupDiscussionActivity extends MessageActivity implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener   {

    private MessagesList messagesList;
    private GetCurrentDetails getCurrentDetails = new GetCurrentDetails();


    @Override
    public void onCreate(Bundle persistentState) {

        super.onCreate(persistentState);
        setContentView(R.layout.activity_inner_group_discussion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.messagesList = findViewById(R.id.messagesList);
        initAdapter();
        toolbar = findViewById(R.id.toolbarInChat);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(group.getGroupName());
        MessageInput input = findViewById(R.id.input_mes);
        input.setInputListener(this);
        input.setAttachmentsListener(this);

        List<Contact> contactList = ContactUtil.getInstance().resolveContacts(group.getMembers(), group.getMemberDetails());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layout.simple_list_item_1, group.getMembers());
        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);


        toolbar.setNavigationOnClickListener(view -> {
            //back to task in group;
            onBackPressed();
        });

        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('@');
        AutocompletePolicy policy1 = new CharPolicy('#');
        // Look for @mentions\
        AutocompletePresenter<Contact> presenter = new UserPresenter(this , contactList);
        AutocompletePresenter<String> presenterTask = new TaskPresenter(this , taskList);

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

        AutocompleteCallback<String> callbackTask = new AutocompleteCallback<String>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, String item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                editable.replace(start, end, item);
                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start+ item.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }

            @Override
            public void onPopupVisibilityChanged(boolean shown) {

            }
        };
        Autocomplete.<Contact>on(input.getInputEditText())
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();

        Autocomplete.<String>on(input.getInputEditText())
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy1)
                .with(presenterTask)
                .with(callbackTask)
                .build();

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
