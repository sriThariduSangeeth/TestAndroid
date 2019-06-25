package app.whatsdone.android.ui.presenter;

import java.util.List;

import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.services.ContactService;
import app.whatsdone.android.services.ContactServiceImpl;

import static app.whatsdone.android.utils.TextUtil.toList;

public class TaskPresenterImpl implements TaskPresenter {
   ContactService contactService = new ContactServiceImpl();

    @Override
    public void checkUserDetails(String mobile) {
        contactService.existsInPlatform(toList(mobile), new ContactService.Listener() {
            @Override
            public void onCompleteSearch(List<ExistUser> users, List<String> isExisting) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
