package app.whatsdone.android.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
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

public class ContactUtil {
    private static final String TAG = ContactUtil.class.getSimpleName();

    public static List<Contact> resolveContacts(List<String> phoneNumbers) {
        List<Contact> items = new ArrayList<>();

        if(phoneNumbers.isEmpty()) return items;

        try{

            items = readContacts(WhatsDoneApplication.getApplication().getApplicationContext(), phoneNumbers);

        }catch (Exception ex){
            Log.w(TAG, ex.getLocalizedMessage());
        }
        return items;
    }

    public static List<Contact> readContacts(Context context, List<String> phoneNumbers) {
        if (context == null)
            return null;
        ContentResolver contentResolver = context.getContentResolver();

        if (contentResolver == null)
            return null;

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
                        if(phoneNumbers.contains(cleanNo(phoneNumber)) && !cleanNo(phoneNumber).isEmpty()){
                            Contact contact = new Contact();
                            contact.setPhoneNumber(cleanNo(phoneNumber));
                            contact.setDisplayName(name);

                            contacts.add(contact);
                        }
                    }
                }
            }
            phones.close();
        }

        return contacts;
    }

    public static String getDisplayNameOrNumber(List<Contact> contacts, String phoneNumber){
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

    public static boolean validate(String phoneNo) {
        if (phoneNo == null || phoneNo.isEmpty()) return false;
        boolean result = false;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(WhatsDoneApplication.getApplication());
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNo, getCurrentLocale());
            result = phoneUtil.isValidNumber(number);
        } catch (NumberParseException ignored) {
        }
        return result;
    }

    private static String getCurrentLocale() {
        String iso = null ;

        TelephonyManager telephonyManager = (TelephonyManager) WhatsDoneApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
// Get network country iso
        if (telephonyManager.getNetworkCountryIso() != null
                && !telephonyManager.getNetworkCountryIso().equals(""))
        {
            iso = telephonyManager.getNetworkCountryIso();
        }

        return iso.toUpperCase();
    }
}
