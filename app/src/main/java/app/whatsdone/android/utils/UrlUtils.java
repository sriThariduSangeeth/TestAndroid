package app.whatsdone.android.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import timber.log.Timber;

public class UrlUtils {
    public static String getUserImage(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isEmpty()) return "";
        try {
            String path = encodeValue(String.format("images/thumbs/u/thumb_%s.jpg", phoneNumber));
            String outputUrl = String.format("%s%s?alt=media", Constants.URL_FIREBASE_STORAGE, path);
            URI uri = URI.create(outputUrl);
            String url =  uri.toASCIIString();
            return url;
        }catch (Exception ex){
            Timber.e(ex);
            return "";
        }


    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
