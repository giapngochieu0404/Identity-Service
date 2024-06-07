package com.hieuubuntu.identityservice.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invalid_tokens")
public class InvalidToken {
    @Id
    private String id;

    private String token;

    @Column(name = "expired_at")
    private Date expiredAt; // Dùng cho job revoke token khi token hết hạn
}
