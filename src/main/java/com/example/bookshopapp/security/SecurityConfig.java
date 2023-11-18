package com.example.bookshopapp.security;

import com.example.bookshopapp.security.jwt.JWTPersistLogoutHandler;
import com.example.bookshopapp.security.jwt.JWTRequestFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8085")
public class SecurityConfig {

    private final BookShopUserDetailsService userDetailsService;
    private final JWTRequestFilter jwtRequestFilter;
    private final JWTPersistLogoutHandler jwtPersistLogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/my").authenticated()
                .antMatchers("/profile").authenticated() //доступ для аутентифицированных
                .antMatchers("/**").permitAll()
                .and()
                .authenticationProvider(authenticationProvider())
                .formLogin()
                .loginPage("/signin")
                .failureUrl("/signin")
                .and()
                .exceptionHandling()
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(jwtPersistLogoutHandler)
                .logoutSuccessUrl("/signin")
                .deleteCookies("token")
                .and()
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/my")
                )
        ;

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //отключение убирается
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
