package app.whatsdone.android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.ExistUser;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ContactPickerListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link ContactPickerListDialogFragment.Listener}.</p>
 */
public class ContactPickerListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_CONTACTS = "contacts";
    private Listener mListener;


    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    // TODO: Customize parameters
    public static ContactPickerListDialogFragment newInstance(ArrayList<ExistUser> contacts) {
        final ContactPickerListDialogFragment fragment = new ContactPickerListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, contacts.size());
        args.putParcelableArrayList(ARG_CONTACTS, contacts);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contactpicker_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = view.findViewById(R.id.contact_list);
        Button contactButton = view.findViewById(R.id.contact_btn);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onContactButtonClicked();
                    ContactPickerListDialogFragment.this.dismiss();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<ExistUser> contacts = getArguments().getParcelableArrayList(ARG_CONTACTS);
        recyclerView.setAdapter(new ContactPickerAdapter(getArguments().getInt(ARG_ITEM_COUNT), contacts));
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
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_contactpicker_list_dialog_item, parent, false));
            text = (TextView) itemView.findViewById(R.id.text);

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onContactPickerClicked(getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class ContactPickerAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;
        private List<ExistUser> contacts;

        ContactPickerAdapter(int itemCount, List<ExistUser> contacts) {
            mItemCount = itemCount;
            this.contacts = contacts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(contacts.get(position).getDisplayName());
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}
