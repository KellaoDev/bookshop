package com.bookshop.services;

import com.bookshop.dto.LoanDTO;
import com.bookshop.entities.*;
import com.bookshop.repositories.CartRepository;
import com.bookshop.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
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

        loan.setBooks(books);
        loanRepository.save(loan);

        cart.getItems().clear();
        cartRepository.save(cart);

        return loan;
    }

    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

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

        return loanRepository.save(loan);
    }


}
