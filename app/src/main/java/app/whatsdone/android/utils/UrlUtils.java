package app.whatsdone.android.utils;

import java.net.URI;

import timber.log.Timber;

public class UrlUtils {
    public static String getUserImage(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isEmpty()) return "";
        try {
            String path = String.format("images/thumbs/u/thumb_%s.jpg?alt=media", phoneNumber);
            String outputUrl = String.format("%s%s", Constants.URL_FIREBASE_STORAGE, path);
            URI uri = URI.create(outputUrl);
            return uri.toASCIIString();
        }catch (Exception ex){
            Timber.e(ex);
            return "";
        }


    }
}
