package com.example.demo.model.persistence;

import com.example.demo.model.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    String jti;

    String deviceName;
    String token;
    Instant expirationDateTime;
    String userAgent;
    String deviceType;
    boolean isRevoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
