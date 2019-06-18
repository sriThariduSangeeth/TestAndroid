package app.whatsdone.android.utils;

import app.whatsdone.android.services.AuthServiceImpl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceFactory {
    public static Retrofit getRetrofitService(){
        AuthServiceImpl.refreshToken();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            String header = "Bearer " + SharedPreferencesUtil.getString(Constants.SHARED_TOKEN);
            System.out.println(header);
            Request request = original.newBuilder()
                    .header("Authorization", header)
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(Constants.URL_FIREBASE)
                .client(client)
                .build();
        return retrofit;
    }
}
