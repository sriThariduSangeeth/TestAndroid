package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import javax.annotation.Nullable;

import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.fragments.GroupFragment;
import app.whatsdone.android.ui.view.DeleteGroupFragmentView;

public class DeleteGroupPresenterImpl implements DeleteGroupPresenter{
    private Activity context;
    private GroupService service = new GroupServiceImpl();
    private DeleteGroupFragmentView view;



    @Override
    public void deleteTeam(String documentId) {
       // ((GroupServiceImpl) service).setContext(context);

        try {
            service.delete(documentId, new ServiceListener() {
                @Override
                public void onSuccess() {
                    view.onGroupDeleted();

                }

                @Override
                public void onError(@Nullable String error) {

                    view.onDeleteError();
                }
            });

        }catch (Exception ex){
            view.onDeleteError();
        }
    }
}
