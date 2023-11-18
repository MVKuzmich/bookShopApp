package com.example.bookshopapp.security.jwt;


import com.example.bookshopapp.data.enums.ContactType;
import com.example.bookshopapp.data.user.UserContactEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.errors.BlackListTokenException;
import com.example.bookshopapp.repository.UserRepository;
import com.example.bookshopapp.security.BookShopUserDetailsService;
import com.example.bookshopapp.security.SecurityUser;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookShopUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException, RuntimeException {
        String token = null;
        String username = null;
        Optional<Cookie[]> cookies = Optional.ofNullable(httpServletRequest.getCookies());
        try {
            if (cookies.isPresent()) {
                token = Arrays.stream(cookies.get())
                        .filter(cookieName -> cookieName.getName().equals("token"))
                        .map(Cookie::getValue)
                        .findAny()
                        .orElse(null);

                if (token != null) {
                    if (jwtRepository.findTokenByTokenValue(token).isPresent()) {
                        throw new BlackListTokenException("invalid token");
                    } else {
                        username = jwtUtil.extractUsername(token);
                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            SecurityUser userDetails =
                                    (SecurityUser) userDetailsService.loadUserByUsername(username);
                            if (jwtUtil.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authenticationToken =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities());

                                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            }
                        }
                    }
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (JwtException ex) {
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, ex);
        }

    }
}
