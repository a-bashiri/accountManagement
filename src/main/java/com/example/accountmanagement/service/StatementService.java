package com.example.accountmanagement.service;

import com.example.accountmanagement.dto.StatementDTO;
import com.example.accountmanagement.exception.ApiException;
import com.example.accountmanagement.model.Statement;
import com.example.accountmanagement.repository.AccountRepository;
import com.example.accountmanagement.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatementService {
    private final StatementRepository statementRepository;
    private final ModelMapper modelMapper;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public ResponseEntity<List<Statement>> getAllStatements(){
        return ResponseEntity.ok(statementRepository.findAll());
    }

    public ResponseEntity<List<StatementDTO>> getAllStatementByParameters(Long accountId,String startDate, String endDate, String startAmount, String endAmount){
        List<Statement> statements = statementRepository.findByAccountId(accountId);
        if (statements.isEmpty())
            throw new ApiException("account id not found", HttpStatus.NOT_FOUND.value());

        if (startDate != null && endDate != null) {
            if (startAmount != null && endAmount != null) {
                statements = filterByDateRangeAndAmountRange(statements, startDate, endDate, startAmount, endAmount);
            } else {
                statements = filterByDateRange(statements, startDate, endDate);
            }
        } else if (startAmount != null && endAmount != null) {
            statements = filterByAmountRange(statements, startAmount, endAmount);
        } else {
            String today = LocalDate.now().format(dateFormatter);
            String threeMonthsAgo = LocalDate.now().minusMonths(3).format(dateFormatter);
            statements = filterByDateRange(statements, threeMonthsAgo, today);
        }
        return ResponseEntity.ok(statements.stream().map(statement -> convertToDTO(statement)).toList());
    }

    private List<Statement> filterByDateRangeAndAmountRange(List<Statement> statements, String startDateStr, String endDateStr, String startAmountStr, String endAmountStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);

        Double startAmount = Double.valueOf(startAmountStr);
        Double endAmount = Double.valueOf(endAmountStr);

        return statements.stream()
                .filter(statement -> {
                    String dateStr = statement.getDatefield();
                    LocalDate accountDate = LocalDate.parse(dateStr, dateFormatter);
                    String amountStr = statement.getAmount();
                    Double amount = Double.valueOf(amountStr);
                    return (!accountDate.isBefore(startDate) && !accountDate.isAfter(endDate))
                            && (amount >= startAmount && amount <= endAmount);
                })
                .collect(Collectors.toList());
    }

    private List<Statement> filterByDateRange(List<Statement> statements, String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);

        return statements.stream()
                .filter(statement -> {
                    String dateStr = statement.getDatefield();
                    LocalDate accountDate = LocalDate.parse(dateStr, dateFormatter);
                    return !accountDate.isBefore(startDate) && !accountDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }

    private List<Statement> filterByAmountRange(List<Statement> statements, String startAmountStr, String endAmountStr) {
        Double startAmount = Double.valueOf(startAmountStr);
        Double endAmount = Double.valueOf(endAmountStr);

        return statements.stream()
                .filter(statement -> {
                    String amountStr = statement.getAmount();
                    Double amount = Double.valueOf(amountStr);
                    return amount >= startAmount && amount <= endAmount;
                })
                .collect(Collectors.toList());
    }

    private StatementDTO convertToDTO(Statement statement){
        return modelMapper.map(statement, StatementDTO.class);
    }
}
