package com.bookshop.services;

import com.bookshop.entities.Book;
import com.bookshop.entities.Cart;
import com.bookshop.entities.CartItem;
import com.bookshop.entities.Loan;
import com.bookshop.repositories.CartRepository;
import com.bookshop.repositories.LoanRepository;
import com.bookshop.services.exceptions.DatabaseException;
import com.bookshop.services.exceptions.ResourceNotFoundException;
import com.bookshop.services.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Service
public class LoanService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private LoanRepository loanRepository;

    public Loan finalizeLoan(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado com o ID: " + cartId));

        if (cart.getItems().isEmpty()) {
            throw new ValidationException("Carrinho vazio");
        }

        if (cart.getUser() == null || cart.getUser().getId() == null) {
            throw new ValidationException("O carrinho não esta associado a nenhum usuário");
        }

        Loan loan = new Loan();
        loan.setUser(cart.getUser());
        loan.setDateLoan(LocalDate.now());
        loan.setDateLoanPredicted(LocalDate.now().plusWeeks(2));
        loan.setReturned(false);
        loan.setFine(0);

        Set<Book> books = new HashSet<>();

        for (CartItem item : cart.getItems()) {
            books.add(item.getBook());
            item.setLoan(loan);
        }

        try {
            loan.setBooks(books);
            loanRepository.save(loan);

            cart.getItems().clear();
            cartRepository.save(cart);

            return loan;
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Carrinho não encontrado com ID: " + cartId);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o carrinho no banco de dados");
        }

    }

    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com o ID: " + loanId));

        loan.setDateLoanReal(LocalDate.now());
        loan.setReturned(true);

        if (loan.getDateLoanReal() != null && loan.getDateLoanReal().isAfter(loan.getDateLoanPredicted())) {
            long daysDelay = ChronoUnit.DAYS.between(loan.getDateLoanPredicted(), loan.getDateLoanReal());

            if (daysDelay > 14) {
                loan.setFine((int) daysDelay * 2);
            } else {
                loan.setFine(0);
            }
        } else {
            loan.setFine(0);
        }

        try {
            return loanRepository.save(loan);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Empréstimo não encontrado com ID: " + loanId);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o empréstimo no banco de dados");
        }

    }


}
