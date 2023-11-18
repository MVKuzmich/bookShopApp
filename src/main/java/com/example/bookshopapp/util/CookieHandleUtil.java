package com.example.bookshopapp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieHandleUtil {

    public static String checkIfFirstOrEndSlashExist(String cookieContents) {
        String result = cookieContents;
        result = result.startsWith("/") ? result.substring(1) : result;
        result = result.endsWith("/") ? cookieContents.substring(0, cookieContents.length() - 1) : result;
        return result;
    }

    public static Cookie removeElementFromCookieValue(String bookSlug, String cookieValue, String cookieName) {
        ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cookieValue.split("/")));
        cookieBooks.remove(bookSlug);
        return new Cookie(cookieName, String.join("/", cookieBooks));
    }

    public static Cookie addElementToCookieValue(String bookSlug, String cookieValue, String cookieName) {
        Cookie cookie = null;
        if (cookieValue == null || cookieValue.isEmpty()) {
            cookie = new Cookie(cookieName, bookSlug);
        } else if (!cookieValue.contains(bookSlug)) {
            cookieValue = cookieValue.concat("/").concat(bookSlug);
            cookie = new Cookie(cookieName, cookieValue);
        }
        return cookie;
    }
    public static List<String> getBookSlugFromCookieValue(String bookContentsCookieValue) {
        return Arrays.stream(CookieHandleUtil.checkIfFirstOrEndSlashExist(bookContentsCookieValue).split("/")).collect(Collectors.toList());
    }

    public static List<String> getBookSlugsFromCookie(String cartContentsCookie, String postponedContentsCookie) {
        List<String> bookSlugs = new ArrayList<>();
        if (cartContentsCookie != null && !cartContentsCookie.isEmpty()) {
            bookSlugs.addAll(CookieHandleUtil.getBookSlugFromCookieValue(cartContentsCookie));
        }
        if (postponedContentsCookie != null && !postponedContentsCookie.isEmpty()) {
            bookSlugs.addAll(CookieHandleUtil.getBookSlugFromCookieValue(postponedContentsCookie));
        }
        return bookSlugs;
    }

}
