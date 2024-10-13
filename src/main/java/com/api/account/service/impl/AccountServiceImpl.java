package com.api.account.service.impl;

import com.api.account.dto.account.AccountCreateDTO;
import com.api.account.dto.account.AccountResponseDTO;
import com.api.account.dto.account.AccountUpdateDTO;
import com.api.account.dto.common.ApiResponseDTO;
import com.api.account.enums.AccountType;
import com.api.account.exception.BadRequestException;
import com.api.account.exception.NotFoundException;
import com.api.account.model.Account;
import com.api.account.repository.AccountRepository;
import com.api.account.service.AccountService;
import com.api.account.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;
import java.util.*;

import static com.api.account.utils.utils.allowedFieldsValidator;
import static com.api.account.utils.utils.getNonNullFields;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final MessageService messageService;
    private final WebClient webClient;

    @Transactional
    public AccountResponseDTO createAccount(AccountCreateDTO accountCreateDTO) throws IllegalAccessException {

        if (accountCreateDTO.getAccountType().getCode().equals(AccountType.INVALID.getCode())) {
            throw new BadRequestException(messageService.getMessage("invalid.account.type"));
        }

        if (accountRepository.existsByAccountNumberAndStatusTrue(accountCreateDTO.getAccountNumber())) {
            throw new BadRequestException(messageService.getMessage("account.number.already.exist"));
        }

        Optional<Account> accountDisabled = accountRepository.findByAccountNumberAndStatusFalse(accountCreateDTO.getAccountNumber());

        if(accountDisabled.isPresent()) {

            AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO();
            accountUpdateDTO.setStatus(true);

            return updateAccount(accountDisabled.get().getId(), accountUpdateDTO);

        }

        webClient.get()
                .uri("/api/clientes/obtenerPorId/{id}", accountCreateDTO.getClientId())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Account account = new Account();
        account.setAccountNumber(accountCreateDTO.getAccountNumber());
        account.setAccountType(accountCreateDTO.getAccountType().getCode());
        account.setBalance(accountCreateDTO.getBalance());
        account.setClientId(accountCreateDTO.getClientId());

        accountRepository.save(account);

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(account, AccountResponseDTO.class);

    }

    public AccountResponseDTO getAccount(Long id) {

        Account account = accountRepository.findByIdAndStatusTrue(id)
                .orElseThrow(
                        () -> new NotFoundException(messageService.getMessage("register.not.found"))
                );

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public AccountResponseDTO updateAccount(Long id, AccountUpdateDTO accountUpdateDTO) throws IllegalAccessException {

        ModelMapper modelMapper = new ModelMapper();

        StringJoiner fieldsNotAllowed = allowedFieldsValidator(accountUpdateDTO, new HashSet<>(Arrays.asList("accountNumber", "accountType", "clientId")));

        if (fieldsNotAllowed.length() > 0) {
            Object[] params = {fieldsNotAllowed.toString()};
            throw new BadRequestException(messageService.getMessageWithParams("field.not.allowed.update", params));
        }

        Map<String, Object> nonNullFields = getNonNullFields(accountUpdateDTO);

        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException(messageService.getMessage("register.not.found"))
        );

        for (Map.Entry<String, Object> entry : nonNullFields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Field field = ReflectionUtils.findField(Account.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, account, value);
            } else {
                Object[] params = {key};
                throw new NotFoundException(messageService.getMessageWithParams("field.not.found", params));
            }
        }

        accountRepository.save(account);

        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public ApiResponseDTO deleteAccount(Long id) throws IllegalAccessException {

        boolean accountExists = accountRepository.existsByIdAndStatusTrue(id);

        if (!accountExists) {
            throw new NotFoundException(messageService.getMessage("register.not.found"));
        }

        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO();
        accountUpdateDTO.setStatus(false);
        updateAccount(id, accountUpdateDTO);

        return new ApiResponseDTO(
                HttpStatus.OK.value(),
                HttpStatus.OK,
                messageService.getMessage("register.deleted")
        );

    }

}
