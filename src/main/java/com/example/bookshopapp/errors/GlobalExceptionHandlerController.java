package com.example.bookshopapp.errors;

import com.example.bookshopapp.dto.BookReviewErrorResponse;
import com.example.bookshopapp.dto.SecurityExceptionResponse;
import com.example.bookshopapp.dto.UserContactConfirmationResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<SecurityExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        SecurityExceptionResponse response = new SecurityExceptionResponse(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({JwtException.class})
    @ResponseBody
    public ResponseEntity<SecurityExceptionResponse> handleJwtException(JwtException ex, HttpServletRequest request) {
        SecurityExceptionResponse response = new SecurityExceptionResponse("Token can not be trust");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    @ResponseBody
    public ResponseEntity<SecurityExceptionResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        SecurityExceptionResponse response = new SecurityExceptionResponse("expired token");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler({BlackListTokenException.class})
    @ResponseBody
    public ResponseEntity<SecurityExceptionResponse> handleBlackListTokenException(BlackListTokenException ex, HttpServletRequest request) {
        SecurityExceptionResponse response = new SecurityExceptionResponse("blacklist token");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    @ExceptionHandler({BookReviewMinLengthException.class})
    @ResponseBody
    public ResponseEntity<BookReviewErrorResponse> handleBookReviewMinLengthException(BookReviewMinLengthException ex, HttpServletRequest request) {

        BookReviewErrorResponse response = new BookReviewErrorResponse(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(UserRegistrationFailureException.class)
    public String handleUserRegistrationFailureException(UserRegistrationFailureException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("registrationFailure", e.getMessage());
        return "redirect:/signin";
    }
}
