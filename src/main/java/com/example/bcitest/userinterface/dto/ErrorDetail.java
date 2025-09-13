package com.example.bcitest.userinterface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorDetail {
    private Instant timestamp;
    private int codigo;
    private String detail;
}
