package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.genre.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    @Query(nativeQuery = true,
            value = "with recursive temp(id, parent_id, slug, name) as (" +
                    "select t1.id, t1.parent_id, t1.slug, t1.name " +
                    "from genres t1 where t1.id = :rootId " +
                    "union all " +
                    "select t2.id, t2.parent_id, t2.slug, t2.name " +
                    "from genres t2 " +
                    "join temp on (temp.id = t2.parent_id)) " +
                    "select id, parent_id, slug, name from temp")
    List<GenreEntity> findAllChildrenByRootId(Integer rootId);

    @Query(nativeQuery = true,
    value = "with recursive temp(id, parent_id, slug, name) as (" +
            "select t1.id, t1.parent_id, t1.slug, t1.name " +
            "from genres t1 where t1.id = :childId " +
            "union " +
            "select t2.id, t2.parent_id, t2.slug, t2.name " +
            "from genres t2 " +
            "join temp on (temp.parent_id = t2.id)) " +
            "select id, parent_id, slug, name from temp")
    List<GenreEntity> findAllParentsByChildId(Integer childId);

    @Query("select g from GenreEntity g where g.parentId is null")
    List<GenreEntity> findAllRootElements();

    @Query("select g from GenreEntity g where g.parentId = :parentId")
    List<GenreEntity> findFirstDescendants(Integer parentId);

    @Query("select g from GenreEntity g where g.slug = :slug")
    GenreEntity findBySlug(String slug);
}
