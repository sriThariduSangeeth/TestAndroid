package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.UrlUtils;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserPresenter extends RecyclerViewPresenter<Contact> {

    protected Adapter adapter;
    private List<Contact> contactList = new ArrayList<>();

    public UserPresenter(Context context , List<Contact> list) {
        super(context);
        this.contactList = list;
    }

    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<Contact> all = contactList;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<Contact> list = new ArrayList<>();
            for (Contact u : all) {
                if (u.getDisplayName().toLowerCase().contains(query) ||
                        u.getPhoneNumber().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("UserPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Contact> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;
            private TextView phoneNumber;
            private CircleImageView userImage;
            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname =  itemView.findViewById(R.id.fullname);
                phoneNumber = itemView.findViewById(R.id.username);
                userImage = itemView.findViewById(R.id.userImage);
            }
        }

        public void setData(List<Contact> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText(holder.fullname.getResources().getString(R.string.no_user_found));
                holder.root.setOnClickListener(null);
                return;
            }
            final Contact user = data.get(position);
            holder.fullname.setText(user.getDisplayName());
            holder.phoneNumber.setText(user.getPhoneNumber());
            if(Constants.SHOW_PHONE_NO_IN_PICKER){
                holder.phoneNumber.setVisibility(View.VISIBLE);
            }
            Picasso.get()
                    .load(UrlUtils.getUserImage(user.getPhoneNumber()))
                    .placeholder(R.drawable.user_group_man_woman3x)
                    .into(holder.userImage);
            holder.root.setOnClickListener(v -> dispatchClick(user));
        }
    }
}
