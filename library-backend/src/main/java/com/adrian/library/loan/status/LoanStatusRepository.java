package com.adrian.library.loan.status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanStatusRepository extends JpaRepository<LoanStatus, Integer> {

    LoanStatus findByName(String name);
}
