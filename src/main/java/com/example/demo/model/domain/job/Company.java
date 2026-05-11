package com.example.demo.model.domain.job;

import com.example.demo.model.common.BaseEntity;
import com.example.demo.model.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "companies")
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //cascade: Định nghĩa việc lan truyền hành động khi thực hiện 1 hành động với entity
    // A (class này) nó sẽ lan truyền hành động được định nghĩa đến B (thuộc tính được khai báo)
    // Khi persist company với logo được set thì nó sẽ persist cả logo

    // orphanRemoval: Định nghĩa entity B (khai báo)
    // khi không còn được tham chiếu trong 1 persistence context, thì sẽ được xóa
    // ví dụ: Company.setLogo(null)/ comRepo.delete(Company) /..
    // để xóa logo thì Xóa ref
    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "logo_id")
    private CompanyLogo logo;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    @Column(name = "job_count")
    @Setter(AccessLevel.NONE)
    private int jobCount;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    // Để xóa 1 member ra khỏi company thì remove khỏi list + set ref user = null (đảm bảo xóa ref ở 2 phía)
    // Chủ yếu nên thực hiện các hành động cascade ở root entity
    @OneToMany(mappedBy = "company")
    private List<User> members;


    public Company(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }
}
