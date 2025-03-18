package com.bookshop.dto;

import java.time.LocalDate;
import java.util.Set;

public class LoanDTO {

    private LocalDate dateLoan;
    private LocalDate dateLoanReal;
    private boolean returned;
    private int fine;
    private Set<Long> bookIds;
    private Long userId;

    public LoanDTO() {
    }

    public LoanDTO(LocalDate dateLoan, LocalDate dateLoanReal, boolean returned, int fine, Set<Long> bookIds, Long userId) {
        this.dateLoan = dateLoan;
        this.dateLoanReal = dateLoanReal;
        this.returned = returned;
        this.fine = fine;
        this.bookIds = bookIds;
        this.userId = userId;
    }

    public LocalDate getDateLoan() {
        return dateLoan;
    }

    public void setDateLoan(LocalDate dateLoan) {
        this.dateLoan = dateLoan;
    }

    public LocalDate getDateLoanReal() {
        return dateLoanReal;
    }

    public void setDateLoanReal(LocalDate dateLoanReal) {
        this.dateLoanReal = dateLoanReal;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public Set<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(Set<Long> bookIds) {
        this.bookIds = bookIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
