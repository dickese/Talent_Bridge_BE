package com.example.demo.model.domain.user;

import com.example.demo.advice.exception.BusinessException;
import com.example.demo.model.common.BaseEntity;
import com.example.demo.model.common.Gender;
import com.example.demo.model.domain.job.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
@ToString
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String fullName;
    private String password;
    private LocalDate dob;
    private String address;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private boolean emailVerified;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "company_id")
    private Company company;

    public void activate(){
        if(this.status != UserStatus.ACTIVE){
            this.status = UserStatus.ACTIVE;
        }
        throw new BusinessException("User is already active");
    }

    public void deactivate(){
        if(this.status != UserStatus.IN_ACTIVE){
            this.status = UserStatus.IN_ACTIVE;
        }
        throw new BusinessException("User is already inactive");
    }

    public void verifyEmail(){
        if(!this.emailVerified){
            this.emailVerified = true;
        }
        throw new BusinessException("User already verify email");
    }

    public User(String email, String fullName, String password, Role role) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
    }
}
