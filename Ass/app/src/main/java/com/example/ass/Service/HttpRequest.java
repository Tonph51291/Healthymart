package com.example.ass.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
        com.example.ass.Service.ApiService requestInterface;

        public HttpRequest() {
                requestInterface = new Retrofit.Builder().baseUrl(ApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(com.example.ass.Service.ApiService.class);

        }
        public com.example.ass.Service.ApiService getApiService() {
                return requestInterface;
        }
}
