package com.example.bookshopapp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.*;

@Value
public class GenreDto {
    Integer id;
    Integer parentId;
    String slug;
    String name;
    Set<GenreDto> children;
    Integer bookCount;

    public GenreDto(Integer id, Integer parentId, String slug, String name, Integer bookCount) {
        this.id = id;
        this.parentId = parentId;
        this.slug = slug;
        this.name = name;
        children = new TreeSet<>(Comparator.comparing(GenreDto::getBookCount).reversed());
        this.bookCount = bookCount;
    }

    public boolean addChild(GenreDto child) {
        return children.add(child);
    }

    public void addAllChild(List<GenreDto> children) {
        this.children.addAll(children);
    }

}
