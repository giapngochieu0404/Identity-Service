package com.hieuubuntu.identityservice.constants.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role implements PersistableEnum<Integer> {
    ADMIN(1, "Admin"),
    USER(2, "User"),
    ;

    private final Integer value;
    private final String name;

    public static Role of(Integer value) {
        return Stream.of(Role.values())
                .filter(item -> item.value.equals(value))
                .findFirst()
                .orElseThrow();
    }

    public static final class Converter implements AttributeConverter<Role, Integer> {

        @Override
        public Integer convertToDatabaseColumn(Role role) {
            return role == null ? null : role.getValue();
        }

        @Override
        public Role convertToEntityAttribute(Integer value) {
            return Role.of(value);
        }
    }
}
