package app.whatsdone.android.ui.fragments;

import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.adapters.SwipeListener;

public interface InnerGroupTaskFragmentListener extends SwipeListener {
    void onContactButtonClicked(Task task);
    void onContactSelected(Task task);
}
