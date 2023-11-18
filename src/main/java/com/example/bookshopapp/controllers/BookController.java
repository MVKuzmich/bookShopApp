package com.example.bookshopapp.controllers;

import com.example.bookshopapp.data.ResourceStorage;
import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.book.review.BookReviewEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.dto.*;
import com.example.bookshopapp.errors.BookReviewMinLengthException;
import com.example.bookshopapp.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/books")
@Slf4j
public class BookController extends BaseController {

    private final ResourceStorage storage;
    private final RatingService ratingService;
    private final BookReviewService bookReviewService;
    private final BookReviewLikeService bookReviewLikeService;

    @Value("${book.review.min-length}")
    private Integer bookReviewMinLength;

    public BookController(BookService bookService, ResourceStorage storage,
                          RatingService ratingService, BookReviewService bookReviewService,
                          UserService userService, BookReviewLikeService bookReviewLikeService) {
        super(bookService, userService);
        this.storage = storage;
        this.ratingService = ratingService;
        this.bookReviewService = bookReviewService;
        this.userService = userService;
        this.bookReviewLikeService = bookReviewLikeService;
    }


    @GetMapping("/{slug}")
    public String chosenBookPage(Principal principal,
                                 @CookieValue(value = "postponedContents", required = false) String postponedContents,
                                 @CookieValue(value = "cartContents", required = false) String cartContents,
                                 @PathVariable("slug") String slug,
                                 Model model) {

        List<RateRangeDto> bookRateRangeList = ratingService.getBookRateRangeList(slug);
        model.addAttribute("bookRateCreateDto", new BookRateCreateDto());
        model.addAttribute("book", bookService.getBookUnitDtoByBookSlug(slug));
        model.addAttribute("countRateList", bookRateRangeList);
        model.addAttribute("rateTotalCount", bookRateRangeList.stream().mapToInt(RateRangeDto::getRateCount).sum());
        return "books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookService.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.saveBook(bookToUpdate); //save new path in db here

        return ("redirect:/books/" + slug);
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        log.info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        log.info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        log.info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/rateBook")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public BookRateResponse rateBook(@RequestBody BookRateCreateDto bookRateCreateDto) {
        Book book = bookService.getBookByBookId(Integer.parseInt(bookRateCreateDto.getBookId()));
        boolean result = ratingService.handleBookRate(
                book,
                userService.getCurrentUser(),
                Integer.parseInt(bookRateCreateDto.getValue()));

        return new BookRateResponse(result ? "true" : "false");
    }

    @PostMapping("/bookReview")
    @PreAuthorize("hasRole('USER')")
    public BookReviewResponse reviewBook(@RequestBody BookReviewCreateDto bookReviewCreateDto) {
        if(bookReviewCreateDto.getText().length() < bookReviewMinLength) {
            throw new BookReviewMinLengthException("Отзыв слишком короткий. Напишите, пожалуйста, более развёрнутый отзыв");
        }
        Book book = bookService.getBookBySlug(bookReviewCreateDto.getBookSlug());
        UserEntity user = userService.getCurrentUser();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime formatDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        BookReviewEntity bookReviewEntity = new BookReviewEntity(
                book,
                user,
                formatDate,
                bookReviewCreateDto.getText());
        bookReviewService.saveBookReview(bookReviewEntity);

        return new BookReviewResponse("true");
    }

    @PostMapping("/rateBookReview")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public BookReviewLikeResponse bookReviewLikeOrDislike(@RequestBody ReviewRateDto reviewRateDto) {
        BookReviewEntity bookReviewEntity = bookReviewService.getReviewById(Integer.parseInt(reviewRateDto.getReviewId()));
        UserEntity user = userService.getCurrentUser();

        boolean result = bookReviewLikeService.handleBookReviewLikeOrDislike(bookReviewEntity, user, Short.parseShort(reviewRateDto.getValue()));

        return new BookReviewLikeResponse(result);
    }
}
