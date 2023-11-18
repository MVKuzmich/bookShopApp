package com.example.bookshopapp;

import com.example.bookshopapp.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;


//@Sql({
//        "classpath:sql/authors.sql",
//        "classpath:sql/book2author.sql",
//        "classpath:sql/book2genre.sql",
//        "classpath:sql/book2tag.sql",
//        "classpath:sql/book2user.sql",
//        "classpath:sql/book2user_type.sql",
//        "classpath:sql/book_file.sql",
//        "classpath:sql/book_file_type.sql",
//        "classpath:sql/book_rates.sql",
//        "classpath:sql/book_rates_add_column.sql",
//        "classpath:sql/book_review.sql",
//        "classpath:sql/book_review_like.sql",
//        "classpath:sql/books.sql",
//        "classpath:sql/create-table.sql",
//        "classpath:sql/create-table-jwt_tokens.sql",
//        "classpath:sql/genres.sql",
//        "classpath:sql/tags.sql",
//        "classpath:sql/user_contact.sql",
//        "classpath:sql/users.sql"
//
//})
@IT
public class IntegrationTestBase {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    static void runContainer() {
        container.start();
    }

   @DynamicPropertySource
   static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
   }
}
