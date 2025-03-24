package com.bookshop.repositories;

import com.bookshop.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);

    boolean existsByUserIdAndReturnedFalse(Long userId);

    @Query("SELECT COUNT(l) > 0 FROM Loan l JOIN l.books b WHERE b.id = :bookId AND l.returned = false")
    boolean existsByBookIdAndReturnedFalse(Long bookId);
}
