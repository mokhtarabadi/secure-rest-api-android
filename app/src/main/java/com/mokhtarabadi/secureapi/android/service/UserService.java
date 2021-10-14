package com.mokhtarabadi.secureapi.android.service;

import com.mokhtarabadi.secureapi.android.model.BaseResponse;
import com.mokhtarabadi.secureapi.android.model.CreateUserResponse;
import com.mokhtarabadi.secureapi.android.model.UserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("users/all")
    Call<ArrayList<UserModel>> getAllUsers();

    @POST("users/")
    Call<CreateUserResponse> createUser(@Body UserModel userModel);

    @PUT("users/{id}")
    Call<BaseResponse> updateUser(@Path("id") int id, @Body UserModel userModel);

    @GET("users/{id}")
    Call<UserModel> getUser(@Path("id") int id);

    @DELETE("users/{id}")
    Call<BaseResponse> deleteUser(@Path("id") int id);
}
