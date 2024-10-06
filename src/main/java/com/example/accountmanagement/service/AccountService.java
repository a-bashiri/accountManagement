package com.example.accountmanagement.service;

import com.example.accountmanagement.exception.ApiException;
import com.example.accountmanagement.model.Account;
import com.example.accountmanagement.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity.ok(accountRepository.findAll());
    }

    public ResponseEntity<Account> getAccountById(Long id){
        Account account = accountRepository.findById(id);
        if (account == null){
            throw new ApiException("account with id: " + id + " not found", HttpStatus.NOT_FOUND.value());
        }
        return ResponseEntity.ok(account);
    }
}
