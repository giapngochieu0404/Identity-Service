package com.hieuubuntu.identityservice.entity;

import com.hieuubuntu.identityservice.constants.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    private String username;

    private String password;

    private String fullname;

    @Convert(
            converter = UserStatus.Converter.class
    )
    private UserStatus statusId;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Integer createdBy;

    private Integer modifiedBy;
}
