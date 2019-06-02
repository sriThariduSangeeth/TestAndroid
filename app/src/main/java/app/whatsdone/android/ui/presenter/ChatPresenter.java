package app.whatsdone.android.ui.presenter;

import app.whatsdone.android.ui.view.ChatFragmentView;

public interface ChatPresenter {
    void init(ChatFragmentView view);
    void loadChats();
}
