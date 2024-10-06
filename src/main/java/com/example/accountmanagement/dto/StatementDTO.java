package com.example.accountmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatementDTO {
    private String accountId;
    private String datefield;
    private String amount;
}
