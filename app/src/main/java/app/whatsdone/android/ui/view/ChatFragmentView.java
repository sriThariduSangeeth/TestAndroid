package app.whatsdone.android.ui.view;

import java.util.List;

import app.whatsdone.android.ui.model.Chat;

public interface ChatFragmentView {
    void updateChats(List<Chat> chat);
}
