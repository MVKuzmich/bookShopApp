package com.example.bookshopapp.service;

import com.example.bookshopapp.data.book.links.Book2UserEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.repository.Book2UserRepository;
import com.example.bookshopapp.repository.Book2UserTypeRepository;
import com.example.bookshopapp.repository.BookRepository;
import com.example.bookshopapp.util.CookieHandleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Book2UserService {

    private final Book2UserTypeRepository book2UserTypeRepository;
    private final Book2UserRepository book2UserRepository;
    private final BookRepository bookRepository;


    public String getBookStatusBySlug(String slug) {
        return book2UserTypeRepository.findBookStatusByBookSlug(slug).orElse("UNLINK");
    }

    @Transactional
    public void saveOrUpdateBooksChosenViaCookie(String cookieValue, UserEntity user, String statusCode) {
        List<String> bookSlugs = CookieHandleUtil.getBookSlugFromCookieValue(cookieValue);
        bookSlugs.forEach(slug -> saveOrUpdateBook2User(user, statusCode, slug));
    }

    @Transactional
    public void saveOrUpdateBook2User(UserEntity user, String statusCode, String slug) {
        Optional<Book2UserEntity> book2UserEntityOptional = book2UserRepository.findBook2UserEntityByBookSlug(slug);
        if (book2UserEntityOptional.isEmpty()) {
            book2UserRepository.save(
                    new Book2UserEntity(
                            LocalDateTime.now(),
                            book2UserTypeRepository.findByCode(statusCode),
                            bookRepository.findBySlug(slug),
                            user
                    ));
        } else {
            Book2UserEntity book2UserEntity = book2UserEntityOptional.get();
            book2UserEntity.setTime(LocalDateTime.now());
            book2UserEntity.setType(book2UserTypeRepository.findByCode(statusCode));
            book2UserRepository.save(book2UserEntity);
        }
    }


}
