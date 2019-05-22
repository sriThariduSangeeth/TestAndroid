package whatsdone.app.whatsdone.presenter;

import whatsdone.app.whatsdone.view.ChatFragmentView;

public interface ChatPresenter {
    void init(ChatFragmentView view);
    void loadChats();
}
