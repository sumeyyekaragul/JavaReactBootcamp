package com.bookstore.api.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bookstore.api.entities.Book;
import com.bookstore.api.entities.Category;
import com.bookstore.api.entities.models.ApiResponse;
import com.bookstore.api.entities.models.ResponseMessage;
import com.bookstore.api.entities.requests.BookRequestForPost;
import com.bookstore.api.exceptions.notFoundExceptions.BookNotFoundException;
import com.bookstore.api.repositories.BookRepository;
import com.bookstore.api.services.Abstract.BookService;
import com.bookstore.api.services.Abstract.CategoryService;

@Service
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository; // IoC
    private final CategoryService categoryService;


    public BookServiceImp(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }
    

    @Override
    public ApiResponse<List<Book>> getAllBook() {
        List<Book> list = bookRepository.findAll();
        return ApiResponse.default_OK(list);
    }

    @Override
    public ApiResponse<Book> getOneBook(int id) {
        Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return ApiResponse.default_OK(book);
    }

    @Override
    public ApiResponse<Book> postOneBook(BookRequestForPost bookRequestForPost) {
        // BookRequestForPost -> Book (entity)
        Category category = categoryService
        .getOneCategory(bookRequestForPost.getCategoryId()).getData();
        
        Book book = new Book();
        book.setTitle(bookRequestForPost.getTitle());
        book.setCategory(category);
        
        Book bookAdd = bookRepository.save(book);
        return ApiResponse.default_CREATED(bookAdd);
    }

    @Override
    public ApiResponse<Book> putOneBook(int id, Book book) {
        // Book var mı?
        getOneBook(id); // yoksa hata fırlatır!
        book.setId(id);
        return ApiResponse.default_ACCEPTED(bookRepository.save(book));
    }

    @Override
    public void deleteOneBook(int id) {
        // getOneBook(id); // kitap yoksa hata fırlatır.
        // bookRepository.deleteById(id);
        bookRepository.delete(getOneBook(id).getData());
    }
}