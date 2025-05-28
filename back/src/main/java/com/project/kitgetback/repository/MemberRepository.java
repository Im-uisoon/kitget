package com.project.kitgetback.repository;

import com.project.kitgetback.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    boolean existsBySchoolAndNameAndCode(String school, String name, String code);
}