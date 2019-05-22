package whatsdone.app.whatsdone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import whatsdone.app.whatsdone.R;
import whatsdone.app.whatsdone.model.Chat;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyRecyclerViewHolder> {
    private List<Chat> chatsList;
    private Context context;

    public ChatRecyclerViewAdapter(List<Chat> chats, Context context) {

        this.chatsList = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.chat_recycler_view_layout, viewGroup, false);


        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder myRecyclerViewHolder, int position) {
        myRecyclerViewHolder.chatText.setText(chatsList.get(position).getChatId());


    }


    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private TextView chatText;
       // private TextView chatTime;


        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            chatText = itemView.findViewById(R.id.chat_text);
         //   chatTime = itemView.findViewById(R.id.chat_time);
        }
    }
}
