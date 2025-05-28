package com.project.kitgetback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "list", schema = "MEMBER")
public class MemberEntity {
    @Id
    private Integer id;
    private String school;
    private String name;
    private String code;
    private String auth;
}
