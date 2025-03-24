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
import com.bookshop.repositories.LoanRepository;
import com.bookshop.services.exceptions.BusinessException;
import com.bookshop.services.exceptions.DatabaseException;
import com.bookshop.services.exceptions.ResourceNotFoundException;
import com.bookshop.services.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private LoanRepository loanRepository;

    public List<Book> getAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao buscar todos os livros");
        }
    }

    public Book getBookById(Long id) {
        Optional<Book> obj = bookRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado para id: " + id));
    }

    public List<BookDTO> searchBooks(String query) {
        List<Book> books = bookRepository.searchBooks(query.toLowerCase());
        return books.stream().map(this::convert).collect(Collectors.toList());
    }

    private BookDTO convert(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthors().stream().map(author -> new AuthorDTO(author.getName())).collect(Collectors.toSet()),
                new CategoryDTO(book.getCategory().getId(), book.getCategory().getName())
        );
    }

    @Transactional
    public Book createBook(BookDTO dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("Livro com esse ISBN existente.");
        }
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new ValidationException("Título do livro é obrigatório.");
        }

        if (dto.getIsbn() == null || dto.getIsbn().isEmpty()) {
            throw new ValidationException("ISBN do livro é obrigatório.");
        }

        if (dto.getImageCover() == null || dto.getImageCover().isEmpty()) {
            throw new ValidationException("Imagem de capa é obrigatório.");
        }

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setImageCover(dto.getImageCover());

        Category category = categoryRepository.findByName(dto.getCategory().getName())
                .orElseGet(() -> categoryRepository.save(new Category(dto.getCategory().getName())));
        if (dto.getCategory().getName() == null || dto.getCategory().getName().isEmpty()) {
            throw new ValidationException("Categoria é obrigatório.");
        }
        book.setCategory(category);

        Set<Author> authors = dto.getAuthors().stream()
                .map(authorDTO -> authorRepository.findByName(authorDTO.getName())
                        .orElseGet(() -> authorRepository.save(new Author(authorDTO.getName()))))
                .collect(Collectors.toSet());
        if (dto.getAuthors() == null || dto.getAuthors().isEmpty()) {
            throw new ValidationException("Autor é obrigatório.");
        }
        book.setAuthors(authors);

        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o livro no banco de dados.");
        }
    }

    @Transactional
    public Book updateBook(Long id, BookDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new ValidationException("Título do livro é obrigatório.");
        }

        Optional<Book> existingISBN = bookRepository.findByIsbn(dto.getIsbn());
        if (existingISBN.isPresent() && existingISBN.get().getId().equals(id)) {
            throw new ValidationException("ISBN do livro já cadastrado.");
        }

        if (dto.getImageCover() == null || dto.getImageCover().isEmpty()) {
            throw new ValidationException("Imagem de capa é obrigatório.");
        }
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado para id: " + id));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setImageCover(dto.getImageCover());

        if (dto.getCategory() != null) {
            Category category = categoryRepository.findByName(dto.getCategory().getName())
                    .orElseGet(() -> categoryRepository.save(new Category(dto.getCategory().getName())));
            book.setCategory(category);
        }

        if (dto.getAuthors() != null) {
            Set<Author> authors = dto.getAuthors().stream()
                    .map(authorDTO -> authorRepository.findByName(authorDTO.getName())
                            .orElseGet(() -> authorRepository.save(new Author(authorDTO.getName()))))
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }

        try {
            return bookRepository.save(book);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Livro não encontrado para id: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o livro no banco de dados");
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado para id: " + id));

        if (loanRepository.existsByBookIdAndReturnedFalse(id)) {
            throw new BusinessException("Não é possível excluir um livro porque ele está emprestado");
        }

        try {
            bookRepository.delete(book);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Livro não encontrado para id: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o livro no banco de dados");
        }
    }

}
