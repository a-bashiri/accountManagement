package com.example.accountmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Statement {
    private Long ID;
    private Long accountId;
    private String datefield;
    private String amount;
}
