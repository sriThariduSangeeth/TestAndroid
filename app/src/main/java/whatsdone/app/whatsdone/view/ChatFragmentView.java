package whatsdone.app.whatsdone.view;

import java.util.List;

import whatsdone.app.whatsdone.model.Chat;

public interface ChatFragmentView {
    void updateChats(List<Chat> chat);
}
