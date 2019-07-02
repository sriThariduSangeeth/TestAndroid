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

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.ExistUser;

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

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_contactpicker_list_dialog_item, parent, false));
            text = itemView.findViewById(R.id.text);


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
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.text.setText(contacts.get(position).getDisplayName());
            holder.text.setOnClickListener(v -> {
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
