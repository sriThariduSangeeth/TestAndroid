package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.fragments.ContactPickerListDialogFragment;
import app.whatsdone.android.ui.fragments.EditTaskFragment;
import app.whatsdone.android.ui.fragments.InnerGroupTaskFragmentListener;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.IconFactory;
import app.whatsdone.android.utils.TextDrawable;
import app.whatsdone.android.utils.UrlUtils;

import static app.whatsdone.android.model.Task.TaskStatus.DONE;
import static app.whatsdone.android.model.Task.TaskStatus.IN_PROGRESS;
import static app.whatsdone.android.model.Task.TaskStatus.ON_HOLD;
import static app.whatsdone.android.model.Task.TaskStatus.TODO;
import static app.whatsdone.android.utils.SortUtil.getStatusIndicatorColor;
import static app.whatsdone.android.utils.SortUtil.getStatusIndicatorText;

public class TaskInnerGroupRecyclerViewAdapter extends RecyclerView.Adapter<TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder> {
    private List<BaseEntity> taskList;
    private Context context;
    private Group group;
    private Task task;
    private InnerGroupTaskFragmentListener listener;
    private FragmentManager fragmentManager;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private TaskService taskService = new TaskServiceImpl();

    public TaskInnerGroupRecyclerViewAdapter(List<BaseEntity> tasks, Context context, Group group, InnerGroupTaskFragmentListener listener, FragmentManager fragmentManager) {

        this.taskList = tasks;
        this.context = context;
        this.group = group;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        // this.task = task;
    }

    @NonNull
    @Override
    public TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.swipe_layout_tasks, viewGroup, false);


        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder holder, int position) {

        Task task = (Task) taskList.get(position);

        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        holder.groupTaskText.setText(task.getTitle());
        if (task.isUnreadTask()) {
            holder.groupTaskText.setTypeface(holder.groupTaskText.getTypeface(), Typeface.BOLD);
        } else {
            holder.groupTaskText.setTypeface(holder.groupTaskText.getTypeface(), Typeface.NORMAL);
        }

        TextDrawable icon = IconFactory.getInstance().get(holder.image, task);

        setStatusIcons(holder, task);


        if (task.getDueDate() == null) {
            holder.date.setText(df.format(new Date()));
        } else {
            holder.date.setText(df.format(task.getDueDate()));
        }

        if (task.getAssignedUserImage() != null && !task.getAssignedUserImage().isEmpty() && URLUtil.isValidUrl(task.getAssignedUserImage())) {
            Picasso.get().load(task.getAssignedUserImage()).placeholder(icon).into(holder.image);
        } else {
            holder.image.setImageDrawable(icon);
            //  Picasso.get().load(R.mipmap.ic_user_default).into(myRecyclerViewHolder.image);
        }

        holder.frontLayout.setOnClickListener(v -> {
            holder.swipeLayout.close(false);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment myFragment = EditTaskFragment.newInstance(group, task);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();
        });
        if (task.getStatus() == Task.TaskStatus.DONE) {
            holder.statusIndicator.setVisibility(View.GONE);

        } else {
            holder.statusIndicator.setVisibility(View.VISIBLE);
            holder.statusIndicator.setText(getStatusIndicatorText(task));
            holder.statusIndicator.setBackgroundColor(context.getResources().getColor(getStatusIndicatorColor(task)));
        }
    }

    private void setStatusIcons(@NonNull MyRecyclerViewHolder holder, Task task) {

        holder.swipeLayout.setMinFlingVelocity(50);

        if (task != null) {
            try {
                binderHelper.bind(holder.swipeLayout, task.getDocumentID());
            }catch (NullPointerException ignored){}

        }

        if (task.getStatus() == TODO) {
            holder.swipeLayout.setEnableEdge(SwipeRevealLayout.DRAG_EDGE_LEFT | SwipeRevealLayout.DRAG_EDGE_RIGHT);
            holder.status.setText(R.string.todo);
        }

        if (task.getStatus() == ON_HOLD) {
            holder.swipeLayout.setEnableEdge(SwipeRevealLayout.DRAG_EDGE_LEFT | SwipeRevealLayout.DRAG_EDGE_RIGHT);
            holder.status.setText(R.string.on_hold);
            holder.inprogressBtn.setVisibility(View.VISIBLE);
            holder.onholdBtn.setVisibility(View.GONE);
            holder.doneBtn.setVisibility(View.VISIBLE);
        }

        if (task.getStatus() == DONE) {
            holder.status.setText(R.string.done);
            holder.swipeLayout.setEnableEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
            holder.inprogressBtn.setVisibility(View.GONE);
            holder.onholdBtn.setVisibility(View.GONE);
            holder.doneBtn.setVisibility(View.GONE);
        }

        if (task.getStatus() == Task.TaskStatus.IN_PROGRESS) {
            holder.swipeLayout.setEnableEdge(SwipeRevealLayout.DRAG_EDGE_LEFT | SwipeRevealLayout.DRAG_EDGE_RIGHT);
            holder.status.setText(R.string.in_progress);
            holder.inprogressBtn.setVisibility(View.GONE);
            holder.onholdBtn.setVisibility(View.VISIBLE);
            holder.doneBtn.setVisibility(View.VISIBLE);
        }

        holder.deleteLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(task);
            }
        });

        holder.onholdBtn.setOnClickListener(click -> {
            if(listener != null)
                listener.onChangeStatus(task, ON_HOLD);
        });

        holder.inprogressBtn.setOnClickListener(click -> {
            if(listener != null)
                listener.onChangeStatus(task, IN_PROGRESS);
        });

        holder.doneBtn.setOnClickListener(click -> {
            if(listener != null)
                listener.onChangeStatus(task, DONE);
        });
        System.out.println("members "+group.getMembers());
        holder.image.setOnClickListener(v -> {

            ArrayList<ExistUser> users = (ArrayList<ExistUser>) ContactUtil.getInstance().resolveContacts(group.getMemberDetails());
            ContactPickerListDialogFragment fragment = ContactPickerListDialogFragment.newInstance(users);
            fragment.setListener(new ContactPickerListDialogFragment.Listener() {
                @Override
                public void onContactPickerClicked(int position) {

                    ExistUser user = group.getMemberDetails().get(position);
                    task.setAssignedUserName(user.getDisplayName());
                    task.setAssignedBy(AuthServiceImpl.getCurrentUser().getDocumentID());
                    task.setAssignedUser(user.getPhoneNumber());
                    task.setAssignedUserImage(UrlUtils.getUserImage(task.getAssignedUser()));
                    listener.onContactSelected(task);

                }

                @Override
                public void onContactButtonClicked() {
                    listener.onContactButtonClicked(task);



                }
            });
            fragment.show(fragmentManager, "Contacts");
        });



    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final SwipeRevealLayout swipeLayout;
        private final View frontLayout;
        private final View deleteLayout;
        private final TextView inprogressBtn, onholdBtn, doneBtn;
        private final TextView groupTaskText, date, status;
        private final ImageView image;
        private final TextView statusIndicator;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            groupTaskText = itemView.findViewById(R.id.task_inner_text);
            image = itemView.findViewById(R.id.image_view_task_inner_group);
            date = itemView.findViewById(R.id.task_inner_date);
            status = itemView.findViewById(R.id.status_inner_task);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            frontLayout = itemView.findViewById(R.id.front_layout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            inprogressBtn = itemView.findViewById(R.id.first_status);
            onholdBtn = itemView.findViewById(R.id.second_status);
            doneBtn = itemView.findViewById(R.id.third_status);


        }
    }


    private Callback getCallBack(final ImageView imageView) {
        return new Callback() {
            @Override
            public void onSuccess() {
                imageView.setAlpha(0f);
                imageView.animate().setDuration(500).alpha(1f).start();
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }


}
