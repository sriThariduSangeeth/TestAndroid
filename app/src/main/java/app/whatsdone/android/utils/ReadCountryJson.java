package app.whatsdone.android.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.whatsdone.android.model.CountryJsonKey;

public class ReadCountryJson {

    private ObjectMapper objectMapper = new ObjectMapper();
    public static List<CountryJsonKey> countyArray = new ArrayList<>();


    private ReadCountryJson() {
        super();
    }

    public static ReadCountryJson getInstance() {
        return ReadCountryJson.Holder.INSTANCE;
    }

    private static class Holder {
        private static final ReadCountryJson INSTANCE = new ReadCountryJson();
    }

    public void getStream (InputStream input){

        try {
            InputStreamReader data = new InputStreamReader(input);
            countyArray = Arrays.asList(objectMapper.readValue(data, CountryJsonKey[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
