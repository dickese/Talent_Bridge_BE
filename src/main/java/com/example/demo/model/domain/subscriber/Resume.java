package com.example.demo.model.domain.subscriber;

import com.example.demo.model.common.BaseEntity;
import com.example.demo.model.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "resumes")
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileId;

    private String fileUrl;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Resume(){

    }

    private Resume(String fileId, String fileUrl, String name, User user) {
        this.fileId = fileId;
        this.fileUrl = fileUrl;
        this.name = name;
        this.user = user;
    }

    public static Resume forStoring(String fileId, String fileUrl, String fileName, User user){
        return new Resume(fileId, fileUrl, fileName, user);
    }

    public boolean ownedBy(User user){
        return this.user.equals(user);
    }
}
