package com.bookshop.entities;



import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Loan implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateLoan;
    private LocalDate dateLoanPredicted;
    private LocalDate dateLoanReal;
    private int fine;

    @ManyToMany
    @JoinTable(
            name = "loan_book",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public Loan() {
    }

    public Loan(Long id, LocalDate dateLoan, LocalDate dateLoanPredicted, LocalDate dateLoanReal, int fine, UserEntity user) {
        this.id = id;
        this.dateLoan = dateLoan;
        this.dateLoanPredicted = dateLoanPredicted;
        this.dateLoanReal = dateLoanReal;
        this.fine = fine;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateLoan() {
        return dateLoan;
    }

    public void setDateLoan(LocalDate dateLoan) {
        this.dateLoan = dateLoan;
    }

    public LocalDate getDateLoanPredicted() {
        return dateLoanPredicted;
    }

    public void setDateLoanPredicted(LocalDate dateLoanPredicted) {
        this.dateLoanPredicted = dateLoanPredicted;
    }

    public LocalDate getDateLoanReal() {
        return dateLoanReal;
    }

    public void setDateLoanReal(LocalDate dateLoanReal) {
        this.dateLoanReal = dateLoanReal;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public Set<Book> getBook() {
        return books;
    }

    public void setBook(Set<Book> books) {
        this.books = books;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
