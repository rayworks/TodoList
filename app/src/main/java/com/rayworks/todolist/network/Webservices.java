package com.rayworks.todolist.network;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by Shirley on 11/9/16.
 */

public class Webservices {

    private enum WsHolder {

        SINGLETON(new Webservices());

        private final Webservices webservices;

        WsHolder(Webservices ws) {
            webservices = ws;
        }

        public Webservices getWebservices() {
            return webservices;
        }
    }

    private Webservices() {

    }

    public static Webservices getInstance() {
        return WsHolder.SINGLETON.getWebservices();
    }

    public static final long CONNECTION_TIMEOUT = 30;
    private Retrofit client;

    public interface TodoService {
        @GET("/todo-list")
        Observable<TodoResp> getList();
    }

    public static final String HOST_URL = "http://10.128.58.58:5000";

    /* package */
    synchronized Retrofit getNetworkClient() {
        if (client == null) {

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                                @Override
                                public void log(String message) {
                                    Timber.tag("OKHttp").d(message);
                                }
                            })
                                    .setLevel(HttpLoggingInterceptor.Level.BODY)
                    ).build();

            client = new Retrofit.Builder()
                    .baseUrl(HOST_URL)
                    .client(httpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
        }

        return client;
    }

    public TodoService getTodoService() {
        return getNetworkClient().create(TodoService.class);
    }

}
