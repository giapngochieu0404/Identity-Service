package com.hieuubuntu.IdentityService.entity;

import com.hieuubuntu.IdentityService.constants.enums.Role;
import com.hieuubuntu.IdentityService.constants.enums.UserStatus;
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

    @Convert(
            converter = Role.Converter.class
    )
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Integer createdBy;

    private Integer modifiedBy;
}
