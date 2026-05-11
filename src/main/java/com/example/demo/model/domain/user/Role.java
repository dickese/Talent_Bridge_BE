package com.example.demo.model.domain.user;

import com.example.demo.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role extends BaseEntity {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;
    private boolean active;


    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @jakarta.persistence.JoinColumn(name = "role_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "permission_id")
    )
    private java.util.Set<Permission> permissions;

    @OneToMany(mappedBy = "role")
    private java.util.Set<User> users;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
