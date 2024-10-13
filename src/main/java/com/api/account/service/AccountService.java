package com.api.account.service;

import com.api.account.dto.account.AccountCreateDTO;
import com.api.account.dto.account.AccountResponseDTO;
import com.api.account.dto.account.AccountUpdateDTO;
import com.api.account.dto.common.ApiResponseDTO;

public interface AccountService {

    AccountResponseDTO createAccount(AccountCreateDTO accountCreateDTO);
    AccountResponseDTO getAccount(Long id);
    AccountResponseDTO updateAccount(Long id, AccountUpdateDTO accountUpdateDTO) throws IllegalAccessException;
    ApiResponseDTO deleteAccount(Long id) throws IllegalAccessException;
}
