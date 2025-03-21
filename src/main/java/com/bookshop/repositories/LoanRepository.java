package com.bookshop.repositories;

import com.bookshop.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);

    boolean existsByUserIdAndReturnedFalse(Long userId);

    boolean existsByBookIdAndReturnedFalse(Long id);
}
