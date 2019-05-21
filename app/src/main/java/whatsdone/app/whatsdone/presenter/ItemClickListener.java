package whatsdone.app.whatsdone.presenter;

import android.view.View;

public interface ItemClickListener {
   // void onItemClick(int position);
    void onClick(View view, int position, boolean isLongClick);
}
