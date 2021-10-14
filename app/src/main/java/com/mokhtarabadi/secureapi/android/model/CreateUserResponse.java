package com.mokhtarabadi.secureapi.android.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserResponse extends BaseResponse {

    private int id;
}
