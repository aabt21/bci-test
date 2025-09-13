package com.example.bcitest.infrastructure.mapper;

import com.example.bcitest.domain.model.Phone;
import com.example.bcitest.infrastructure.database.PhoneEntity;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper {

    public static Phone toDomain(PhoneEntity phoneEntity) {
        Phone phone = new Phone();
        phone.setNumber(phoneEntity.getNumber());
        phone.setCityCode(phoneEntity.getCityCode());
        phone.setCountryCode(phoneEntity.getCountryCode());
        return phone;
    }

    public static PhoneEntity toEntity(Phone phone) {
        PhoneEntity phoneEntity = new PhoneEntity();
        phoneEntity.setNumber(phone.getNumber());
        phoneEntity.setCityCode(phone.getCityCode());
        phoneEntity.setCountryCode(phone.getCountryCode());
        return phoneEntity;
    }
}
