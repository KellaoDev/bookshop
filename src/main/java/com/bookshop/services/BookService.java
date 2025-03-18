package com.bookshop.services;

import com.bookshop.dto.AuthorDTO;
import com.bookshop.dto.BookDTO;
import com.bookshop.dto.CategoryDTO;
import com.bookshop.entities.Author;
import com.bookshop.entities.Book;
import com.bookshop.entities.Category;
import com.bookshop.repositories.AuthorRepository;
import com.bookshop.repositories.BookRepository;
import com.bookshop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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

    public Book createBook(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setImageCover(dto.getImageCover());

        Category category = categoryRepository.findByName(dto.getCategory().getName())
                .orElseGet(() -> categoryRepository.save(new Category(dto.getCategory().getName())));
        book.setCategory(category);

        Set<Author> authors = dto.getAuthors().stream()
                .map(authorDTO -> authorRepository.findByName(authorDTO.getName())
                        .orElseGet(() -> authorRepository.save(new Author(authorDTO.getName()))))
                .collect(Collectors.toSet());

        book.setAuthors(authors);

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setImageCover(dto.getImageCover());

        if (dto.getCategory() != null) {
            Category category = categoryRepository.findByName(dto.getCategory().getName())
                    .orElseGet(() -> categoryRepository.save(new Category(dto.getCategory().getName())));
            book.setCategory(category);
        }

        Set<Author> authors = dto.getAuthors().stream()
                .map(authorDTO -> authorRepository.findByName(authorDTO.getName())
                        .orElseGet(() -> authorRepository.save(new Author(authorDTO.getName()))))
                .collect(Collectors.toSet());
        book.setAuthors(authors);

        return bookRepository.save(book);
    }



}
