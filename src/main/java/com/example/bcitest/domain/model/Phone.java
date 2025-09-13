package com.example.bcitest.domain.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Phone {
    private Long number;
    private Integer cityCode;
    private String countryCode;
}
