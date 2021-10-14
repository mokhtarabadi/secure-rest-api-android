package com.mokhtarabadi.secureapi.android.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserModel {
    private Integer id;
    private String username;
    @NonNull private String name;
}
