package com.example.bookshopapp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SearchAspect {

    @Pointcut("execution(public * com.example.bookshopapp.service.BookService.getPageOfSearchResultsBooks(..))")
    public void isSearchBooksByName() {
    }

    @Pointcut("execution(public * com.example.bookshopapp.service.BookService.getRecentBooksBetweenDesc(..))")
    public void isSearchBooksByPublishDate() {}


    @Before("isSearchBooksByName()")
    public void addLoggingForBookNameRequested(JoinPoint joinPoint) {
        log.info("User search request book name: {}", joinPoint.getArgs()[0]);
    }

    @Before("isSearchBooksByPublishDate()")
    public void addLoggingForBookPublishDateRequested(JoinPoint joinPoint) {
        log.info("User search request book publish date: {} - {}", joinPoint.getArgs()[0], joinPoint.getArgs()[1]);
    }
}
