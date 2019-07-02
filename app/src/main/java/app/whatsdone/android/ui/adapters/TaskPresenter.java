package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Task;
import timber.log.Timber;

public class TaskPresenter extends RecyclerViewPresenter<String> {

    protected Adapter adapter;
    private List<Task> tasksList;

    public TaskPresenter(Context context , List<Task> taskList) {
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
        List<Task> all = tasksList;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<Task> list = new ArrayList<>();
            for (Task u : all) {
                if (u.getTitle().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Timber.d("found " + list.size() + " users for query " + query);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<TaskPresenter.Adapter.Holder> {

        private List<Task> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;
            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = itemView.findViewById(R.id.taskname);
            }
        }

        public void setData(List<Task> data) {
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
                holder.fullname.setText(getContext().getString(R.string.no_task));
                holder.root.setOnClickListener(null);
                return;
            }
            final Task task = data.get(position);
            holder.fullname.setText(task.getTitle());
            holder.root.setOnClickListener(v -> dispatchClick(task.getTitle()));
        }
    }
}
