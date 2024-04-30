package com.hieuubuntu.identityservice.constants.enums;

import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum UserStatus implements PersistableEnum<Integer>{
    ACTIVE(1, "Đang hoạt động"),
    LOCK(2, "Khóa"),
    INACTIVE(3, "Ngừng hoạt động")
    ;

    private final Integer value;
    private final String name;

    public static UserStatus of(Integer value) {
        return Stream.of(UserStatus.values())
                .filter(item -> item.value.equals(value))
                .findFirst()
                .orElseThrow();
    }

    public static final class Converter implements AttributeConverter<UserStatus, Integer> {

        @Override
        public Integer convertToDatabaseColumn(UserStatus userStatus) {
           return userStatus == null ? null : userStatus.getValue();
        }

        @Override
        public UserStatus convertToEntityAttribute(Integer value) {
           return UserStatus.of(value);
        }
    }
}
