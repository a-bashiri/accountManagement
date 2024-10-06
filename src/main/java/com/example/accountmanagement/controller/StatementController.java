package com.example.accountmanagement.controller;

import com.example.accountmanagement.dto.StatementDTO;
import com.example.accountmanagement.exception.ApiException;
import com.example.accountmanagement.model.Statement;
import com.example.accountmanagement.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/statement")
@RequiredArgsConstructor
public class StatementController {
    private final StatementService statementService;

    @GetMapping("/all")
    public ResponseEntity<List<Statement>> getAllStatements(){
        return statementService.getAllStatements();
    }

    @GetMapping
    public ResponseEntity<List<StatementDTO>> getAllStatementByParameters(
            @RequestParam Long accountId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String startAmount,
            @RequestParam(required = false) String endAmount,
            Authentication authentication
    ){
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("USER"))) {
            if (startDate != null || endDate != null || startAmount != null || endAmount != null) {
                throw new ApiException("You are not authorized to use these parameters",HttpStatus.UNAUTHORIZED.value());
            }
        }
        return statementService.getAllStatementByParameters(accountId,startDate,endDate,startAmount,endAmount);
    }
}
