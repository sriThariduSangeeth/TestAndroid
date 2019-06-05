package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import app.whatsdone.android.ui.view.GroupFragmentView;

public interface GroupPresenter {
    void init(GroupFragmentView view, Activity context);
    void subscribe();
    void unSubscribe();

    //void onItemClick(GroupFragmentView view, int position);
    //void onLongItemClick(GroupFragmentView view, int position);

}
