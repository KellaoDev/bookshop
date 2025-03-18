package com.bookshop.resources;

import com.bookshop.entities.Loan;
import com.bookshop.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanResource {

    @Autowired
    private LoanService loanService;

    @PostMapping("/finalize/{cartId}")
    public ResponseEntity<Loan> finalizeLoan(@PathVariable Long cartId) {
        Loan loan = loanService.finalizeLoan(cartId);
        return ResponseEntity.ok().body(loan);
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long loanId) {
        Loan loan = loanService.returnLoan(loanId);
        return ResponseEntity.ok(loan);
    }

}
