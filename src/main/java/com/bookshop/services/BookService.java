package com.bookshop.services;

import com.bookshop.dto.BookDTO;
import com.bookshop.entities.Author;
import com.bookshop.entities.Book;
import com.bookshop.entities.Category;
import com.bookshop.repositories.AuthorRepository;
import com.bookshop.repositories.BookRepository;
import com.bookshop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        Optional<Book> obj = bookRepository.findById(id);
        return obj.orElseThrow();
    }

    public Book createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setImageCover(bookDTO.getImageCover());
        book.setRented(bookDTO.getRented());

        Category category = categoryRepository.findByName(bookDTO.getCategory().getName())
                .orElseGet(() -> categoryRepository.save(new Category(bookDTO.getCategory().getName())));
        book.setCategory(category);

        Set<Author> authors = bookDTO.getAuthors().stream()
                .map(authorDTO -> authorRepository.findByName(authorDTO.getName())
                        .orElseGet(() -> authorRepository.save(new Author(authorDTO.getName()))))
                .collect(Collectors.toSet());

        book.setAuthors(authors);

        return bookRepository.save(book);
    }


}
