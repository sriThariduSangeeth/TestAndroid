package app.whatsdone.android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.UrlUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactPickerListDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_CONTACTS = "contacts";
    private Listener mListener;


    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public static ContactPickerListDialogFragment newInstance(ArrayList<ExistUser> contacts) {
        final ContactPickerListDialogFragment fragment = new ContactPickerListDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CONTACTS, contacts);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contactpicker_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = view.findViewById(R.id.contact_list);
        Button contactButton = view.findViewById(R.id.contact_btn);

        contactButton.setOnClickListener(v -> {
            if(mListener != null){
                mListener.onContactButtonClicked();
                ContactPickerListDialogFragment.this.dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<ExistUser> contacts = getArguments().getParcelableArrayList(ARG_CONTACTS);
        recyclerView.setAdapter(new ContactPickerAdapter(contacts.size(), contacts));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if(mListener == null) {
            if (parent != null) {
                mListener = (Listener) parent;
            } else {
                mListener = (Listener) context;
            }
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onContactPickerClicked(int position);
        void onContactButtonClicked();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private View root;
        private TextView fullName;
        private TextView phoneNumber;
        private CircleImageView userImage;
        ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            fullName =  itemView.findViewById(R.id.fullname);
            phoneNumber = itemView.findViewById(R.id.username);
            userImage = itemView.findViewById(R.id.userImage);
        }

    }

    private class ContactPickerAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;
        private List<ExistUser> contacts;

        ContactPickerAdapter(int itemCount, List<ExistUser> contacts) {
            mItemCount = itemCount;
            this.contacts = contacts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.user, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ExistUser user = contacts.get(position);
            holder.fullName.setText(user.getDisplayName());
            holder.phoneNumber.setText(user.getPhoneNumber());
            if(Constants.SHOW_PHONE_NO_IN_PICKER){
                holder.phoneNumber.setVisibility(View.VISIBLE);
            }
            Picasso.get()
                    .load(UrlUtils.getUserImage(user.getPhoneNumber()))
                    .placeholder(R.drawable.user_group_man_woman3x)
                    .into(holder.userImage);
            holder.root.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onContactPickerClicked(position);
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}
