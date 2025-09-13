package com.example.bcitest.domain.model;

import lombok.Data;

@Data
public class Phone {
    private Long number;
    private Integer cityCode;
    private String countryCode;
}
