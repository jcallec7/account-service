package com.api.account.controller;

import com.api.account.dto.account.AccountCreateDTO;
import com.api.account.dto.account.AccountResponseDTO;
import com.api.account.dto.account.AccountUpdateDTO;
import com.api.account.dto.common.ApiResponseDTO;
import com.api.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/cuentas")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/crear")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid AccountCreateDTO accountCreateDTO) throws IllegalAccessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountCreateDTO));
    }

    @GetMapping("/obtenerPorId/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PatchMapping("/actualizar/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long id, @RequestBody @Valid AccountUpdateDTO clientUpdateDTO) throws IllegalAccessException {
        return ResponseEntity.ok(accountService.updateAccount(id,clientUpdateDTO));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDTO> deleteAccount(@PathVariable Long id) throws IllegalAccessException {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }

}
