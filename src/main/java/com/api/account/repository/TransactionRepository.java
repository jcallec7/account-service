package com.api.account.repository;

import com.api.account.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<List<Transaction>> findByAccountClientIdAndDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

}
