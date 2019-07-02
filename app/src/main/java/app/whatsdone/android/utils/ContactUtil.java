package app.whatsdone.android.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import app.whatsdone.android.WhatsDoneApplication;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.services.AuthServiceImpl;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import timber.log.Timber;

public class ContactUtil {
    private static final String TAG = ContactUtil.class.getSimpleName();
    private Map<String, String> contacts = new Hashtable<>();
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

    public Map<String, String> getContacts() {
        return contacts;
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

    public List<Contact> resolveContacts(List<String> phoneNumbers, List<ExistUser> existUsers) {
        List<Contact> items = new ArrayList<>();

        if(phoneNumbers.isEmpty()) return items;

        try{
            if(contacts.size() == 0)
               readContacts(WhatsDoneApplication.getApplication().getApplicationContext());
            items = filterContacts(phoneNumbers, existUsers);
        }catch (Exception ex){
            Timber.tag(TAG).w(ex.getLocalizedMessage());
        }
        return items;
    }

    public List<ExistUser> resolveContacts(List<ExistUser> existUsers) {

        List<ExistUser> items = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        for (ExistUser user : existUsers) {
            phoneNumbers.add(user.getPhoneNumber());
        }


        if(existUsers.isEmpty()) return items;

        try{
            if(contacts.size() == 0)
                readContacts(WhatsDoneApplication.getApplication().getApplicationContext());
            List<Contact> users = filterContacts(phoneNumbers, existUsers);
            for (Contact user : users) {
                ExistUser existUser = new ExistUser();
                existUser.setPhoneNumber(user.getPhoneNumber());
                existUser.setDisplayName(user.getDisplayName());
                items.add(existUser);
            }
        }catch (Exception ex){
            Timber.tag(TAG).w(ex.getLocalizedMessage());
        }
        return items;
    }

    public Contact resolveContact(String phoneNumber,@Nullable List<ExistUser> members) {
        if(members == null)
            members = new ArrayList<>();
        List<Contact> items;
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phoneNumber);
        Contact contact = new Contact();
        contact.setPhoneNumber(phoneNumber);
        contact.setDisplayName(phoneNumber);

        if(TextUtil.isNullOrEmpty(phoneNumber))
            return contact;

        try{
            if(contacts.size() == 0)
                readContacts(WhatsDoneApplication.getApplication().getApplicationContext());
            items = filterContacts(phoneNumbers, members);

            if(items.size()>0)
                contact = items.get(0);
        }catch (Exception ex){
            Timber.tag(TAG).w(ex.getLocalizedMessage());
        }
        return contact;
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

    public void getPermission(Activity activity){
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if(contacts.isEmpty())
                                readContacts(WhatsDoneApplication.getApplication().getApplicationContext());
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {

                    }
                })
                .onSameThread()
                .check();
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

    private List<Contact> filterContacts(List<String> numbers, List<ExistUser> existUsers){
        ExistUser existUser = new ExistUser();

        String name = AuthServiceImpl.getCurrentUser().getDisplayName();
        String number = AuthServiceImpl.getCurrentUser().getPhoneNo();

        List<Contact> items = new ArrayList<>();
        Map<String, String> memberDetails = new Hashtable<>();

        if(existUsers!=null) {
            for (ExistUser user : existUsers) {
                memberDetails.put(user.getPhoneNumber(), user.getDisplayName());

                if (user.getIsInvited()) {
                    memberDetails.put(user.getPhoneNumber(), user.getPhoneNumber() +"(INVITED)");
                }
            }
        }

        for (String contact:numbers) {
            Contact item = new Contact();

            String contactNo = cleanNo(contact);
            String contactItem = contacts.get(contactNo);

            item.setDisplayName(contactNo);
            item.setPhoneNumber(contactNo);

            if( contactNo.equals(number))
            {
                item.setDisplayName(name);
            }

            else if(contactItem != null ){
                item.setDisplayName(contactItem);
            }
            else
            {
                if(memberDetails.containsKey(contactNo)){
                    item.setDisplayName(memberDetails.get(contactNo));
                }
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
