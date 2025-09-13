package com.example.bcitest.infrastructure.database;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PhoneEntity {
    @Id
    private Long number;
    private Integer cityCode;
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
