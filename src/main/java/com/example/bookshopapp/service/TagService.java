package com.example.bookshopapp.service;

import com.example.bookshopapp.data.tag.TagEntity;
import com.example.bookshopapp.dto.TagDto;
import com.example.bookshopapp.dto.TagUnitDto;
import com.example.bookshopapp.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagDto getTagByTagId(Integer tagId) {
        return tagRepository.findByTagId(tagId);
    }

    public List<TagUnitDto> getAllTagsDto() {
        return tagRepository.findAllDto();
    }

    public List<TagDto> getTagsByBookId(Integer bookId) {
        return tagRepository.findTagsByBookId(bookId);
    }
}
