package com.hieuubuntu.identityservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @JsonProperty("expired_at")
    private Date expiredAt; // Dùng cho job revoke token khi token hết hạn
}
