package com.project.kitgetback.repository;

import com.project.kitgetback.entity.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> {
    boolean existsByEmail(String email);
    Optional<MembershipEntity> findByEmail(String email);
}
