package com.example.bookshopapp.service;

import com.example.bookshopapp.data.enums.ContactType;
import com.example.bookshopapp.data.user.UserContactEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.errors.UserRegistrationFailureException;
import com.example.bookshopapp.repository.*;
import com.example.bookshopapp.dto.UserContactConfirmationPayload;
import com.example.bookshopapp.dto.UserContactConfirmationResponse;
import com.example.bookshopapp.dto.UserRegistrationForm;
import com.example.bookshopapp.security.BookShopUserDetailsService;
import com.example.bookshopapp.security.SecurityUser;
import com.example.bookshopapp.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final UserContactRepository contactRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BookShopUserDetailsService userDetailsService;

    @Transactional
    public void registerNewUser(UserRegistrationForm userRegistrationForm) throws UserRegistrationFailureException {
        UserEntity userByEmail = userRepository.findUserByContact(userRegistrationForm.getEmail());
        UserEntity userByPhone = userRepository.findUserByContact(userRegistrationForm.getPhone());
        if (userByEmail != null || userByPhone != null) {
            throw new UserRegistrationFailureException("such an user exist! Please, log in!");
        } else {
            UserEntity user = userRepository.save(new UserEntity(
                    passwordEncoder.encode(userRegistrationForm.getPassword()),
                    LocalDateTime.now(),
                    0,
                    userRegistrationForm.getName()
            ));
            contactRepository.save(new UserContactEntity(
                    user, ContactType.EMAIL, (short) 1, "111", 3, LocalDateTime.now(), userRegistrationForm.getEmail()));
            contactRepository.save(new UserContactEntity(
                    user, ContactType.PHONE, (short) 1, "111", 3, LocalDateTime.now(), userRegistrationForm.getPhone()));

        }
    }

    public UserContactConfirmationResponse login(UserContactConfirmationPayload payload) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                        payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserContactConfirmationResponse response = new UserContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public UserContactConfirmationResponse jwtLogin(UserContactConfirmationPayload payload) throws UsernameNotFoundException {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityUser userDetails =
                (SecurityUser) userDetailsService.loadUserByUsername(payload.getContact());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtil.generateToken(userDetails);
        UserContactConfirmationResponse response = new UserContactConfirmationResponse();
        response.setResult(jwtToken);

        return response;
    }



    public UserEntity getCurrentUser() {
        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            email = (String) oAuth2User.getAttributes().get("email");
        }
        return (email == null) ? ((SecurityUser) authentication.getPrincipal()).getUserEntity() : userRepository.findUserByContact(email);
    }
}
