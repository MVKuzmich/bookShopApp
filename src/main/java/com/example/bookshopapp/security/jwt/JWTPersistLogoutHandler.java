package com.example.bookshopapp.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JWTPersistLogoutHandler implements LogoutHandler {

    private final JwtRepository jwtRepository;
    private final JWTUtil jwtUtil;

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if(request.getCookies() != null) {
            Optional<Cookie> jwtOptional = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("token")).findFirst();
            if(jwtOptional.isPresent()) {
                Cookie jwt = jwtOptional.get();
                String token = jwt.getValue();
                LocalDate expirationDate = new java.sql.Date(jwtUtil.extractExpiration(token).getTime()).toLocalDate();
                jwtRepository.saveAndFlush(new JwtToken(token, expirationDate));
            }
        } else {
            response.sendRedirect("/");
        }
    }
}
