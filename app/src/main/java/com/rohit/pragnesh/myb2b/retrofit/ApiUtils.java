package com.rohit.pragnesh.myb2b.retrofit;

public class ApiUtils {
    public static final String BASE_URL = "http://pragnesh.pythonanywhere.com";

    private ApiUtils() {
    }

    public static APIService getAPIService() {

        return RetrofitClient.getClient().create(APIService.class);
    }
}
