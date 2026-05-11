package com.example.demo.model.persistence.emailToken;

import com.example.demo.advice.exception.BusinessException;
import com.example.demo.model.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailVerificationToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String token;
    private Instant expiredAt;
    @Setter(AccessLevel.NONE)
    private boolean used;


    public void use(){
        if(!this.used){
            this.used = true;
        }
        throw new BusinessException("This token is already used");
    }

    public boolean isExpired(){
        return this.expiredAt.isAfter(Instant.now());
    }
}
