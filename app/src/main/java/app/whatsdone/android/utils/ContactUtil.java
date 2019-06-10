package app.whatsdone.android.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import app.whatsdone.android.WhatsDoneApplication;
import app.whatsdone.android.model.Contact;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import timber.log.Timber;

public class ContactUtil {
    private static final String TAG = ContactUtil.class.getSimpleName();
    private static List<Contact> contacts = new ArrayList<>();
    private static MyContentObserver contentObserver = new MyContentObserver();

    public static class MyContentObserver extends ContentObserver {
        public MyContentObserver() {
            super(null);
        }
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            String[] fieldListProjection = {
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };
            Cursor cursor = WhatsDoneApplication.getApplication().getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, fieldListProjection, null, null,ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " Desc");
            List<Contact> newContacts = getContacts(cursor);
            for (Contact contact : newContacts) {
                if(!contacts.contains(contact)){
                    contacts.add(contact);
                }else {
                    for (Contact old : contacts) {
                        if(old.getPhoneNumber().equals(contact.getPhoneNumber())){
                            old.setDisplayName(contact.getDisplayName());
                        }
                    }
                }
            }
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    }

    public static List<Contact> resolveContacts(List<String> phoneNumbers) {
        List<Contact> items = new ArrayList<>();

        if(phoneNumbers.isEmpty()) return items;

        try{
            if(contacts.size() == 0)
                contacts = readContacts(WhatsDoneApplication.getApplication().getApplicationContext(), phoneNumbers);
            items = filterContacts(phoneNumbers);
        }catch (Exception ex){
            Timber.tag(TAG).w(ex.getLocalizedMessage());
        }
        return items;
    }

    public static List<Contact> readContacts(Context context, List<String> phoneNumbers) {
        if (context == null)
            return null;
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.registerContentObserver(
                        ContactsContract.Contacts.CONTENT_URI, true, contentObserver);

        String[] fieldListProjection = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor phones = contentResolver
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        , fieldListProjection,null, null, null, null);
        List<Contact> contacts = getContacts(phones);

        return contacts;
    }

    @NonNull
    private static List<Contact> getContacts(Cursor phones) {
        HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
        List<Contact> contacts = new ArrayList<>();
        if (phones != null && phones.getCount() > 0) {
            while (phones.moveToNext()) {
                String normalizedNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                if (Integer.parseInt(phones.getString(phones.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //if(phoneNumbers.contains(cleanNo(phoneNumber)) && !cleanNo(phoneNumber).isEmpty()){
                            Contact contact = new Contact();
                            contact.setPhoneNumber(cleanNo(phoneNumber));
                            contact.setDisplayName(name);

                            contacts.add(contact);
                        //}
                    }
                }
            }
            phones.close();
        }
        return contacts;
    }

    private static List<Contact> filterContacts(List<String> numbers){
        List<Contact> items = new ArrayList<>();
        for (Contact contact:contacts) {
            if(numbers.contains(contact.getPhoneNumber())){
                items.add(contact);
            }
        }

        return items;
    }

    public static String getDisplayNameOrNumber(List<Contact> contacts, String phoneNumber){
        if(phoneNumber == null || phoneNumber.isEmpty()) return "";
        String contactName = phoneNumber;
        for (Contact contactItem:contacts) {
            if(contactItem.getPhoneNumber().equals(ContactUtil.cleanNo(phoneNumber))){
                contactName = contactItem.getDisplayName();
            }
        }

        return contactName;
    }

    public static String cleanNo(String phoneNo) {
        if (phoneNo == null || phoneNo.isEmpty()) return null;
        String result = "";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(WhatsDoneApplication.getApplication());
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNo, getCurrentLocale());
            result = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException ignored) {
        }
        return result;
    }

    private static String getCurrentLocale() {
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
