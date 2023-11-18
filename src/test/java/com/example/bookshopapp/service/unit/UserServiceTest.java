package com.example.bookshopapp.service.unit;

import com.example.bookshopapp.data.user.UserContactEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.dto.UserContactConfirmationPayload;
import com.example.bookshopapp.dto.UserContactConfirmationResponse;
import com.example.bookshopapp.dto.UserRegistrationForm;
import com.example.bookshopapp.errors.UserRegistrationFailureException;
import com.example.bookshopapp.repository.UserContactRepository;
import com.example.bookshopapp.repository.UserRepository;
import com.example.bookshopapp.security.BookShopUserDetailsService;
import com.example.bookshopapp.security.SecurityUser;
import com.example.bookshopapp.security.jwt.JWTUtil;
import com.example.bookshopapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserContactRepository contactRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private BookShopUserDetailsService userDetailsService;

    @InjectMocks
    private UserService userService;
    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;
    @Captor
    private ArgumentCaptor<UserContactEntity> contactCaptor;

    private UserRegistrationForm userRegistrationForm;
    private UserContactConfirmationPayload payload;

    @BeforeEach
    void init() {
        userRegistrationForm =
                UserRegistrationForm.builder()
                        .name("Mick")
                        .email("mick@gmail.com")
                        .password("123456")
                        .phone("+375441111111")
                        .build();

        payload = new UserContactConfirmationPayload("mick@gmail.com", "123456");
    }

    @Test
    void testRegisterNewUser_success() {

        UserEntity savedUser = new UserEntity(userRegistrationForm.getPassword(), LocalDateTime.now(), 0, "Mick");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.registerNewUser(userRegistrationForm);

        verify(userRepository).save(userCaptor.capture());
        assertEquals("Mick", userCaptor.getValue().getName());

        verify(contactRepository, times(2)).save(contactCaptor.capture());
        assertEquals(2, contactCaptor.getAllValues().size());
        assertEquals("mick@gmail.com", contactCaptor.getAllValues().get(0).getContact());
        assertEquals("+375441111111", contactCaptor.getAllValues().get(1).getContact());

    }

    @Test
    void testRegisterNewUser_exception() {

        UserEntity existingUser = new UserEntity(null, null, 0, "Existing User");
        when(userRepository.findUserByContact(anyString())).thenReturn(existingUser);

        assertThrows(UserRegistrationFailureException.class, () -> {
            userService.registerNewUser(userRegistrationForm);
        });

    }

    @Test
    void testJwtLogin_success() {
        SecurityUser user = new SecurityUser(new UserEntity(
                "hash",
                LocalDateTime.now(),
                0,
                "mick@gmail.com"
        ));
        String token = "token";
        UserContactConfirmationResponse response = new UserContactConfirmationResponse();
        response.setResult(token);
        when(userDetailsService.loadUserByUsername(payload.getContact())).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn(token);

        assertThat(userService.jwtLogin(payload).getResult()).isEqualTo(response.getResult());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(any(UserDetails.class));
        verify(userDetailsService, times(1)).loadUserByUsername(payload.getContact());
    }

    @Test
    void testJwtLogin_invalidCredentials() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(AuthenticationException.class, () -> {
            userService.jwtLogin(payload);
        });
        verifyNoInteractions(jwtUtil, userDetailsService);
    }

    @Test
    void testJwtLogin_userNotFound() {
        when(userDetailsService.loadUserByUsername(any(String.class))).thenThrow(UsernameNotFoundException.class);
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(null, null, null));
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.jwtLogin(payload);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtUtil);
        verify(userDetailsService, times(1)).loadUserByUsername(any(String.class));
    }
}