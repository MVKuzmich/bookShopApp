package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.tag.TagEntity;
import com.example.bookshopapp.dto.TagDto;
import com.example.bookshopapp.dto.TagUnitDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    @Query("select new com.example.bookshopapp.dto.TagDto(t.id, t.description) from TagEntity t " +
            "where t.id = :tagId")
    TagDto findByTagId(Integer tagId);

    @Query(value = "select result.id, result.description, " +
            "concat(" +
            "cast(15 + (round(cast(result.portion as numeric) * 10/result.total) * 5) as varchar), 'px') as size " +
            "from " +
            "(select t.id, t.description, count(t.id) as portion, sum(t.id) as total " +
            "from tags t " +
            "join book2tag b2t on b2t.tag_id = t.id " +
            "group by t.id, t.description) as result",
            nativeQuery = true)
    List<TagUnitDto> findAllDto();


    @Query("select new com.example.bookshopapp.dto.TagDto(t.id, t.description) " +
            "from TagEntity t " +
            "join t.book2TagEntitySet b2t " +
            "join b2t.tag " +
            "where b2t.book.id = :bookId")
    List<TagDto> findTagsByBookId(Integer bookId);
}
