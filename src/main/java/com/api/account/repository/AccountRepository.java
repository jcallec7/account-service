package com.api.account.repository;

import com.api.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumberAndStatusTrue(String accountNumber);
    Optional<Account> findByAccountNumberAndStatusFalse(String accountNumber);
    boolean existsByIdAndStatusTrue(Long id);
    Optional<Account> findByIdAndStatusTrue(Long id);

}
