package com.example.bookshopapp.service.unit;

import com.example.bookshopapp.dto.*;
import com.example.bookshopapp.mapper.AuthorMapper;
import com.example.bookshopapp.repository.AuthorRepository;
import com.example.bookshopapp.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    @Spy
    private AuthorService authorService;

    private AuthorUnitModelDto authorUnitModelDto;

    @BeforeEach()
    void init() {
        authorUnitModelDto = AuthorUnitModelDto.builder()
                .id(1)
                .photo(null)
                .slug("aaa")
                .name("JRR Tolkien")
                .description("author")
                .countBooksByAuthor(15L)
                .build();
    }

    @Test
    void getAuthorsMap() {
        when(authorRepository.findAllAuthorDto()).thenReturn(List.of(
                new AuthorDto("aaa", "Petrov Ivan"),
                new AuthorDto("bbb", "Petrov Adam"),
                new AuthorDto("ccc", "Ivanov Sergey")
                ));

        Map<String, List<AuthorDto>> authorsMap = authorService.getAuthorsMap();
        assertThat(authorsMap.keySet()).isEqualTo(Set.of("I", "P"));
        assertThat(authorsMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())).hasSize(3);

        verify(authorRepository, times(1)).findAllAuthorDto();
    }

    @Test
    void getAuthorUnitDtoBySlug() {
        doReturn(List.of(new BookModelDto(), new BookModelDto())).when(authorService).getBooksByAuthorSlug(anyString(), any(Integer.class), any(Integer.class));
        when(authorRepository.findAuthorBySlug("abc")).thenReturn(Optional.of(authorUnitModelDto));
        when(authorMapper.toAuthorUnitDto(eq(authorUnitModelDto), anyList())).thenReturn(new AuthorUnitDto());

        authorService.getAuthorUnitDtoBySlug("abc", 0, 5);

        verify(authorRepository, times(1)).findAuthorBySlug(anyString());
        verify(authorMapper, times(1)).toAuthorUnitDto(any(AuthorUnitModelDto.class), anyList());
        verify(authorService, times(1)).getBooksByAuthorSlug(anyString(), any(Integer.class), any(Integer.class));

    }

//    @Test
//    void getBooksByAuthorSlug() {
//        List<BookDto> books = List.of(new BookModelDto(), new BookModelDto());
//        when(authorRepository.findBooksByAuthorSLug(eq("abc"), any(PageRequest.class))).thenReturn(books);
//
//        List<BookDto> actualBooks = authorService.getBooksByAuthorSlug("abc", 0, 5).getContent();
//
//        assertThat(actualBooks).hasSize(books.size());
//        verify(authorRepository).findBooksByAuthorSLug(anyString(), any(PageRequest.class));
//    }
}