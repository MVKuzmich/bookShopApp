package com.example.bookshopapp.security;

import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookShopUserDetailsService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String contact) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByContact(contact);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Username or password does not exist");
        }
        return new SecurityUser(userEntity);
    }
}

