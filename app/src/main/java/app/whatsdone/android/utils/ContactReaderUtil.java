package app.whatsdone.android.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.fragments.ContactSelectedListener;

public class ContactReaderUtil {
       private Intent data;
    private Context context;
    private Task task;

    public ContactReaderUtil(Intent data, Context context, Task task) {
        this.data = data;
        this.context = context;
        this.task = task;
    }

    public void selectContact(@Nullable ContactReadListner listener) {
        Uri contactData = data.getData();
        Cursor c = context.getContentResolver().query(contactData, null, null, null, null);
        if (c.moveToFirst()) {
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String num = "";
            Set<String> oneContact = new HashSet<>();

            if (Integer.valueOf(hasNumber) == 1) {
                Cursor numbers = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (numbers.moveToNext()) {

                    String assignee = numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String assignee_name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    task.setAssignedUserName(assignee_name);
                    task.setAssignedUser(ContactUtil.getInstance().cleanNo(assignee));
                    String num1 = assignee.replaceAll("\\s+", "");
                    oneContact.add(num1);
                }

                numbers.close();
                selectOneContact(oneContact, new ContactSelectedListener() {
                    @Override
                    public void onSelected(String number) {
                        //contactName.add(name);
                        number = ContactUtil.getInstance().cleanNo(number);
                        if (number != null && !number.isEmpty()) {

                            task.setAssignedUserName(number);
                            if(listener != null){
                                listener.onTaskSelected(task);
                            }
                        }

                    }

                });
            }
        }
    }

    private void selectOneContact(Set<String> oneContact, ContactSelectedListener listener) {
        String[] numbers = oneContact.toArray(new String[oneContact.size()]);

        if (numbers.length == 0)
            return;

        if (numbers.length == 1) {
            listener.onSelected(numbers[0]);
            return;
        }
        AlertDialog.Builder contactDialog = new AlertDialog.Builder(context);
        contactDialog.setTitle("Select one contact to add");
        contactDialog.setItems(numbers, (dialog, which) -> listener.onSelected(numbers[which]));
        contactDialog.show();

    }
}
