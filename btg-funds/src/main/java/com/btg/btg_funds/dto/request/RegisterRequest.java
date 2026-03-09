package com.btg.btg_funds.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String username;
    private String password;

    private String name;
    private String email;
    private String phone;

    private String notificationPreference;
}
