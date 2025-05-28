package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "members", schema = "member")
public class MembershipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String password;
    private String school;
    private String name;
    private String code;
    private String auth;
}
