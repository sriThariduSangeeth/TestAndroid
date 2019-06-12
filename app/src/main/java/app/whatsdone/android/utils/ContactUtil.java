package app.whatsdone.android.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import app.whatsdone.android.WhatsDoneApplication;
import app.whatsdone.android.model.Contact;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import timber.log.Timber;

public class ContactUtil {
    private static final String TAG = ContactUtil.class.getSimpleName();
    private Dictionary<String, String> contacts = new Hashtable<>();
    private MyContentObserver contentObserver = new MyContentObserver();
    private boolean isObserved = false;
    private static final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
    };
    private String locale = getCurrentLocale();
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(WhatsDoneApplication.getApplication());

    private ContactUtil() {}

    public void cleanNo(List<String> members) {
        for (int i = 0; i < members.size(); i++) {
            String cleaned = cleanNo(members.get(i));
            members.set(i, cleaned);
        }
    }

    private static class LazyHolder {
        static final ContactUtil INSTANCE = new ContactUtil();
    }
    public static ContactUtil getInstance() {
        return LazyHolder.INSTANCE;
    }

    public class MyContentObserver extends ContentObserver {
        MyContentObserver() {
            super(null);
        }
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            CursorLoader cursorLoader = new CursorLoader(WhatsDoneApplication.getApplication(),
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + ">0 AND LENGTH(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ")>0",
                    null,
                    ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " Desc");

            Cursor cursor = cursorLoader.loadInBackground();
            getContacts(cursor);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    }

    public List<Contact> resolveContacts(List<String> phoneNumbers) {
        List<Contact> items = new ArrayList<>();

        if(phoneNumbers.isEmpty()) return items;

        try{
            if(contacts.size() == 0)
               readContacts(WhatsDoneApplication.getApplication().getApplicationContext());
            items = filterContacts(phoneNumbers);
        }catch (Exception ex){
            Timber.tag(TAG).w(ex.getLocalizedMessage());
        }
        return items;
    }

    private void readContacts(Context context) {
        if (context == null)
            return;

        //addObserver(context.getContentResolver());

        CursorLoader cursorLoader = new CursorLoader(context,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + ">0 AND LENGTH(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ")>0",
                null,
                "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ")ASC");

        Cursor cursor = cursorLoader.loadInBackground();
        getContacts(cursor);
    }

    private void addObserver(ContentResolver contentResolver) {
        if(!isObserved) {
            contentResolver.registerContentObserver(
                    ContactsContract.Contacts.CONTENT_URI, true, contentObserver);
            isObserved = true;
        }
    }

    private void getContacts(Cursor cursor) {


        if (cursor != null && cursor.moveToFirst()) {

            int Number = cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int Name = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            do {

                try {
                    String number = cursor.getString(Number);
                    String name = cursor.getString(Name);

                    addContact(name, number);

                } catch (Exception e) {
                    Timber.e(e);
                }

            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    private void addContact(String name, String phoneNumber) {
        contacts.put(cleanNo(phoneNumber), name);
    }

    private List<Contact> filterContacts(List<String> numbers){
        List<Contact> items = new ArrayList<>();
        for (String contact:numbers) {
            String contactNo = cleanNo(contact);
            String contactItem = contacts.get(contactNo);
            Contact item = new Contact();
            item.setDisplayName(contactNo);
            item.setPhoneNumber(contactNo);

            if(contactItem != null){
                item.setDisplayName(contactItem);
                item.setPhoneNumber(contactNo);
            }

            items.add(item);
        }

        return items;
    }

    public String getDisplayNameOrNumber(List<Contact> contacts, String phoneNumber){
        if(phoneNumber == null || phoneNumber.isEmpty()) return "";
        String contactName = phoneNumber;
        for (Contact contactItem:contacts) {
            if(contactItem.getPhoneNumber().equals(cleanNo(phoneNumber))){
                contactName = contactItem.getDisplayName();
            }
        }

        return contactName;
    }

    public String cleanNo(String phoneNo) {
        if (phoneNo == null || phoneNo.isEmpty()) return null;
        String result = "";
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNo, locale);
            result = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException ignored) {
        }
        return result;
    }

    public boolean validate(String phoneNo) {
        if (phoneNo == null || phoneNo.isEmpty()) return false;
        boolean result = false;
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNo, locale);
            result = phoneUtil.isValidNumber(number);
        } catch (NumberParseException ignored) {
        }
        return result;
    }

    private String getCurrentLocale() {
        String iso = "" ;

        TelephonyManager telephonyManager = (TelephonyManager) WhatsDoneApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null
                && !telephonyManager.getNetworkCountryIso().equals(""))
        {
            iso = telephonyManager.getNetworkCountryIso();
        }

        return iso.toUpperCase();
    }
}
