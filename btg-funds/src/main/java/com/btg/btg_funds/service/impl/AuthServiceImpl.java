package com.btg.btg_funds.service.impl;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.UserDocument;
import com.btg.btg_funds.dto.request.LoginRequest;
import com.btg.btg_funds.dto.request.RegisterRequest;
import com.btg.btg_funds.dto.response.AuthResponse;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.repository.ClientRepository;
import com.btg.btg_funds.repository.UserRepository;
import com.btg.btg_funds.security.JwtService;
import com.btg.btg_funds.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           ClientRepository clientRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void register(RegisterRequest request) {

        userRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new BusinessException("El username ya se encuentra registrado");
                });

        ClientDocument client = new ClientDocument();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setNotificationPreference(request.getNotificationPreference());
        client.setBalance(500000.0);

        ClientDocument savedClient = clientRepository.save(client);

        UserDocument user = new UserDocument();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of("CLIENT"));
        user.setActive(true);
        user.setClientId(savedClient.getId());

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDocument user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRoles(),
                user.getClientId()
        );

        return new AuthResponse(token);
    }
}
