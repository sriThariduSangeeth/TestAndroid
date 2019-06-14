package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.whatsdone.android.R;

public class ContactPickerBottomSheetFragment extends BottomSheetDialogFragment {

    public interface Listener {
       void onItemSelected(int index);
       void onContactButtonSelected();
       void onCancelSelected();
    }
    private static int RESULT_LOAD_IMAGE = 1;
    public ContactPickerBottomSheetFragment()
    {

    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
        LinearLayout gallery = v.findViewById(R.id.gallary_holder);
        LinearLayout camera = v.findViewById(R.id.camera_holder);

        gallery.setOnClickListener(v12 -> {
        });

        camera.setOnClickListener(v1 -> {
        });
        return v;
    }


}
