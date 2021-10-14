package com.mokhtarabadi.secureapi.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mokhtarabadi.secureapi.android.databinding.ActivityMainBinding;
import com.mokhtarabadi.secureapi.android.model.BaseResponse;
import com.mokhtarabadi.secureapi.android.model.CreateUserResponse;
import com.mokhtarabadi.secureapi.android.model.UserModel;
import com.mokhtarabadi.secureapi.android.service.UserService;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Slf4j
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        val binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        val application = (MainApplication) getApplication();
        val userService = application.getRetrofit().create(UserService.class);

        // create new user
        binding.create.setOnClickListener(
                v -> {
                    val user = new UserModel("Mohammad");
                    user.setUsername("mokhtarabadi");
                    userService
                            .createUser(user)
                            .enqueue(
                                    new Callback<CreateUserResponse>() {
                                        @Override
                                        public void onResponse(
                                                Call<CreateUserResponse> call,
                                                Response<CreateUserResponse> response) {
                                            log.info(
                                                    "user created with id: {}",
                                                    response.body().getId());
                                        }

                                        @Override
                                        public void onFailure(
                                                Call<CreateUserResponse> call, Throwable t) {
                                            log.error("failed to create user", t);
                                        }
                                    });
                });

        // update user
        binding.update.setOnClickListener(
                v -> {
                    val user = new UserModel("Ali");
                    userService
                            .updateUser(1, user)
                            .enqueue(
                                    new Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(
                                                Call<BaseResponse> call,
                                                Response<BaseResponse> response) {
                                            log.info("response: {}", response.body());
                                        }

                                        @Override
                                        public void onFailure(
                                                Call<BaseResponse> call, Throwable t) {
                                            log.error("failed to update user", t);
                                        }
                                    });
                });

        // get user
        binding.get.setOnClickListener(
                v -> {
                    userService
                            .getUser(1)
                            .enqueue(
                                    new Callback<UserModel>() {
                                        @Override
                                        public void onResponse(
                                                Call<UserModel> call,
                                                Response<UserModel> response) {
                                            log.info("user: {}", response.body());
                                        }

                                        @Override
                                        public void onFailure(Call<UserModel> call, Throwable t) {
                                            log.error("failed to get user", t);
                                        }
                                    });
                });

        // delete user
        binding.delete.setOnClickListener(
                v -> {
                    userService
                            .deleteUser(1)
                            .enqueue(
                                    new Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(
                                                Call<BaseResponse> call,
                                                Response<BaseResponse> response) {
                                            log.info("response: {}", response.body());
                                        }

                                        @Override
                                        public void onFailure(
                                                Call<BaseResponse> call, Throwable t) {
                                            log.error("failed to delete user", t);
                                        }
                                    });
                });

        // get all
        binding.all.setOnClickListener(
                v -> {
                    userService
                            .getAllUsers()
                            .enqueue(
                                    new Callback<ArrayList<UserModel>>() {
                                        @Override
                                        public void onResponse(
                                                Call<ArrayList<UserModel>> call,
                                                Response<ArrayList<UserModel>> response) {
                                            for (UserModel userModel : response.body()) {
                                                log.info("user: {}", userModel);
                                            }
                                        }

                                        @Override
                                        public void onFailure(
                                                Call<ArrayList<UserModel>> call, Throwable t) {
                                            log.error("failed to get all users", t);
                                        }
                                    });
                });
    }
}
