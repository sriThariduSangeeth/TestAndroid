package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;


import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;

public class TaskPresenter extends RecyclerViewPresenter<String> {

    protected Adapter adapter;
    private List<String> tasksList = new ArrayList<>();

    public TaskPresenter(Context context , List<String> taskList) {
        super(context);
        this.tasksList = taskList;
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
        adapter = new TaskPresenter.Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<String> all = tasksList;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<String> list = new ArrayList<>();
            for (String u : all) {
                if (u.toLowerCase().contains(query) ||
                        u.toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("UserPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<TaskPresenter.Adapter.Holder> {

        private List<String> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;
            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = ((TextView) itemView.findViewById(R.id.taskname));
            }
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @Override
        public TaskPresenter.Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TaskPresenter.Adapter.Holder(LayoutInflater.from(getContext()).inflate(R.layout.tasks, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(TaskPresenter.Adapter.Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("No Task here!");
                holder.root.setOnClickListener(null);
                return;
            }
            final String task = data.get(position);
            holder.fullname.setText(task);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dispatchClick(task);
                }
            });
        }
    }
}
