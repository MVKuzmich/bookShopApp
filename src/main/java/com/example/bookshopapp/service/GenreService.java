package com.example.bookshopapp.service;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.genre.GenreEntity;
import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.dto.GenreDto;
import com.example.bookshopapp.mapper.BookMapper;
import com.example.bookshopapp.repository.AuthorRepository;
import com.example.bookshopapp.repository.BookRepository;
import com.example.bookshopapp.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<GenreDto> getAllGenres() {
        List<GenreEntity> rootEntityList = genreRepository.findAllRootElements();
        List<GenreDto> genreDtoList = new ArrayList<>();
        for (GenreEntity rootEntity : rootEntityList) {
            List<GenreEntity> entityChildren = genreRepository.findAllChildrenByRootId(rootEntity.getId());
            entityChildren.remove(0);
            if (entityChildren.isEmpty()) {
                genreDtoList.add(convertToGenreDto(rootEntity));
            } else {
                genreDtoList.add(getGenreDto(rootEntity));
            }
        }
        genreDtoList.sort(Comparator.comparing(GenreDto::getBookCount).reversed());
        return genreDtoList;
    }

    private GenreDto getGenreDto(GenreEntity parentEntity) {
        List<GenreEntity> entityChildren = new ArrayList<>(genreRepository.findFirstDescendants(parentEntity.getId()));
        GenreDto genreDtoParent = convertToGenreDto(parentEntity);
        if (entityChildren.stream().allMatch(child -> genreRepository.findFirstDescendants(child.getId()).isEmpty())) {
            genreDtoParent.addAllChild(entityChildren.stream().map(this::convertToGenreDto).collect(Collectors.toList()));
        } else {
            for (GenreEntity entityChild : entityChildren) {
                List<GenreEntity> descendants = genreRepository.findFirstDescendants(entityChild.getId());
                if (descendants.isEmpty()) {
                    genreDtoParent.addChild(convertToGenreDto(entityChild));
                } else {
                    genreDtoParent.addChild(getGenreDto(entityChild));
                }
            }
        }
        return genreDtoParent;
    }

    private GenreDto convertToGenreDto(GenreEntity entity) {
        return new GenreDto(entity.getId(), entity.getParentId(), entity.getSlug(), entity.getName(), getBookListByGenre(entity, new HashSet<>()).size());

    }

    public List<BookDto> getAllBooksByGenre(String slug, Integer offset, Integer limit) {
        List<Book> bookList;
        GenreEntity genreEntity = genreRepository.findBySlug(slug);
        if(genreRepository.findFirstDescendants(genreEntity.getId()).isEmpty()) {
            bookList = new ArrayList<>(genreEntity.getBookSet());
        } else {
            bookList = new ArrayList<>(getBookListByGenre(genreEntity, new HashSet<>()));
        }
        return bookList.stream()
                .map(book -> bookMapper.toBookDto(book, bookRepository.findAuthorsByBookId(book.getId())))
                .skip((offset < 1) ? offset : (long) offset * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Set<Book> getBookListByGenre(GenreEntity genreEntity, Set<Book> resultSet) {
        resultSet.addAll(genreEntity.getBookSet());
        List<GenreEntity> children = new ArrayList<>(genreRepository.findFirstDescendants(genreEntity.getId()));
        if (children.stream().allMatch(descendant -> genreRepository.findFirstDescendants(descendant.getId()).isEmpty())) {
            resultSet.addAll(children.stream()
                    .flatMap(childEntity -> childEntity.getBookSet().stream())
                    .collect(Collectors.toSet()));
        } else {
            for (GenreEntity entityChild : children) {
                resultSet.addAll(getBookListByGenre(entityChild, resultSet));
            }
        }
        return resultSet;
    }

    public GenreEntity getGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }

    public List<GenreEntity> getAllParentsBySlug(String slug) {
        List<GenreEntity> genreEntityList = genreRepository.findAllParentsByChildId(getGenreBySlug(slug).getParentId());
        Collections.reverse(genreEntityList);
        return genreEntityList;
    }
}
