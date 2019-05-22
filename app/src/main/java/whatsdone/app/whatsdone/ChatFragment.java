package whatsdone.app.whatsdone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.adapters.ChatRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.Chat;
import whatsdone.app.whatsdone.presenter.ChatPresenter;
import whatsdone.app.whatsdone.presenter.ChatPresenterImpl;
import whatsdone.app.whatsdone.view.ChatFragmentView;


public class ChatFragment extends Fragment implements ChatFragmentView {
    private List<Chat> chatsList = new ArrayList<>();
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private ChatPresenter chatPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Chat> chats = new ArrayList<>();
        for (int i = 0; i<10 ; i++)
        {
            Chat chat = new Chat();
            chat.setChatId("Chat " +i);
            chats.add(chat);
        }

        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(chats, getContext());
        chatRecyclerView.setAdapter(chatRecyclerViewAdapter);

        this.chatPresenter = new ChatPresenterImpl();
        this.chatPresenter.init(this);
        this.chatPresenter.loadChats();

        return view;
    }

    @Override
    public void updateChats(List<Chat> chats) {

        this.chatsList = chats;
        chatRecyclerViewAdapter.notifyDataSetChanged();
    }
}
