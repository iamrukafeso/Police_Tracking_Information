package com.rukayat_oyefeso.police_tracking_information;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static  final String BASE_URL = "https://newsapi.org/v2/";
    private static Client Client;
    private    static Retrofit retrofit;
    private  Client()
    {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized Client getInstance()
    {
        if (Client == null)
        {
            Client = new Client();
        }
        return Client;
    }
    public Interface getApi()
    {
        return retrofit.create(Interface.class);
    }
}
