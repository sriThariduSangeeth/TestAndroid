package app.whatsdone.android.utils;

import android.app.Activity;
import android.app.AlertDialog;

import app.whatsdone.android.R;

public class AlertUtil {
    public static void showAlert(Activity context, String error){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.alert));
        alert.setMessage(error);

        alert.setPositiveButton(context.getString(android.R.string.ok), (dialog, which) -> {
        });
        alert.setCancelable(false);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.show();
    }
}
