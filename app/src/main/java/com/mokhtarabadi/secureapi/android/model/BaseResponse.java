package com.mokhtarabadi.secureapi.android.model;

import lombok.Data;

@Data
public class BaseResponse {
    private boolean success;
    private String errorMessage;
}
