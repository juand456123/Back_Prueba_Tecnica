package com.btg.btg_funds.service;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.UserDocument;
import com.btg.btg_funds.dto.request.LoginRequest;
import com.btg.btg_funds.dto.request.RegisterRequest;
import com.btg.btg_funds.dto.response.AuthResponse;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.repository.ClientRepository;
import com.btg.btg_funds.repository.UserRepository;
import com.btg.btg_funds.security.JwtService;
import com.btg.btg_funds.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void deberiaRegistrarUsuarioCorrectamente() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("juan");
        request.setPassword("123456");
        request.setName("Juan Diego");
        request.setEmail("juan@test.com");
        request.setPhone("3001234567");
        request.setNotificationPreference("EMAIL");

        when(userRepository.findByUsername("juan")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");

        ClientDocument savedClient = new ClientDocument();
        savedClient.setId("client123");
        savedClient.setName("Juan Diego");
        savedClient.setEmail("juan@test.com");
        savedClient.setPhone("3001234567");
        savedClient.setNotificationPreference("EMAIL");
        savedClient.setBalance(500000.0);

        when(clientRepository.save(any(ClientDocument.class))).thenReturn(savedClient);

        authService.register(request);

        ArgumentCaptor<ClientDocument> clientCaptor = ArgumentCaptor.forClass(ClientDocument.class);
        verify(clientRepository, times(1)).save(clientCaptor.capture());

        ClientDocument clientGuardado = clientCaptor.getValue();
        assertEquals("Juan Diego", clientGuardado.getName());
        assertEquals("juan@test.com", clientGuardado.getEmail());
        assertEquals("3001234567", clientGuardado.getPhone());
        assertEquals("EMAIL", clientGuardado.getNotificationPreference());
        assertEquals(500000.0, clientGuardado.getBalance());

        ArgumentCaptor<UserDocument> userCaptor = ArgumentCaptor.forClass(UserDocument.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserDocument userGuardado = userCaptor.getValue();
        assertEquals("juan", userGuardado.getUsername());
        assertEquals("encoded-password", userGuardado.getPassword());
        assertEquals(List.of("CLIENT"), userGuardado.getRoles());
        assertTrue(userGuardado.getActive());
        assertEquals("client123", userGuardado.getClientId());

        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    void deberiaLanzarExcepcionCuandoUsernameYaExiste() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("juan");
        request.setPassword("123456");
        request.setName("Juan Diego");
        request.setEmail("juan@test.com");
        request.setPhone("3001234567");
        request.setNotificationPreference("EMAIL");

        UserDocument existingUser = new UserDocument();
        existingUser.setId("user1");
        existingUser.setUsername("juan");

        when(userRepository.findByUsername("juan")).thenReturn(Optional.of(existingUser));

        assertThrows(BusinessException.class, () -> authService.register(request));

        verify(clientRepository, never()).save(any());
        verify(userRepository, never()).save(any(UserDocument.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deberiaHacerLoginCorrectamenteYRetornarToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("juan");
        request.setPassword("123456");

        UserDocument user = new UserDocument();
        user.setId("user1");
        user.setUsername("juan");
        user.setRoles(List.of("CLIENT"));
        user.setClientId("client123");

        when(userRepository.findByUsername("juan")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("juan", List.of("CLIENT"), "client123"))
                .thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(request);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("juan");
        verify(jwtService, times(1))
                .generateToken("juan", List.of("CLIENT"), "client123");

        assertNotNull(response);
    }

    @Test
    void deberiaLanzarExcepcionCuandoUsuarioNoExisteEnLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("juan");
        request.setPassword("123456");

        when(userRepository.findByUsername("juan")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> authService.login(request));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString(), anyList(), anyString());
    }
}
