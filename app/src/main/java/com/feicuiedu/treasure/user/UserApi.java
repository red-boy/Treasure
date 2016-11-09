package com.feicuiedu.treasure.user;

import com.feicuiedu.treasure.user.login.LoginResult;
import com.feicuiedu.treasure.user.register.RegisterResult;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 *
 * 将用户模块API，转为Java接口
 */
public interface UserApi {

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);

}