package com.feicuiedu.treasure.net;

import com.feicuiedu.treasure.user.UserApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetClient {

    public static final String BASE_URL = "http://admin.syfeicuiedu.com";

    private static NetClient netClient;

    private final OkHttpClient client;
    private final Retrofit retrofit;

    private NetClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                        // 添加gson转换器(注意要加依赖)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static NetClient getInstance() {
        if (netClient == null) {
            netClient = new NetClient();
        }
        return netClient;
    }

    private UserApi userApi;

    /**
     * 获取用户模型API对象
     */
    public UserApi getUserApi() {
        if (userApi == null) {
            // retrofit核心代码
            // 将http api转化的java接口进行代码构建(根据注解等)
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }
    /**
     * 获取宝藏API对象
     */

    public OkHttpClient getClient() {
        return client;
    }
}
