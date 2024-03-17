package com.adrian.library.borrowing.status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingStatusRepository extends JpaRepository<BorrowingStatus, Integer> {

    BorrowingStatus findByName(String name);
}
