package app.whatsdone.android.ui.presenter;

import app.whatsdone.android.ui.view.SettingsView;

public class SettingsPresenterImpl implements SettingsPresenter {
    private final SettingsView view;

    public SettingsPresenterImpl(SettingsView view){

        this.view = view;
    }

    @Override
    public void save() {

    }

    @Override
    public void onImageEdit() {
        this.view.onImageEdit();
    }

    @Override
    public void syncContacts() {

    }

    @Override
    public void logout() {

    }

}
