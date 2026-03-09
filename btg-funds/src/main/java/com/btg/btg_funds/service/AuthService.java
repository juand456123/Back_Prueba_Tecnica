package com.btg.btg_funds.service;

import com.btg.btg_funds.dto.request.LoginRequest;
import com.btg.btg_funds.dto.request.RegisterRequest;
import com.btg.btg_funds.dto.response.AuthResponse;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
