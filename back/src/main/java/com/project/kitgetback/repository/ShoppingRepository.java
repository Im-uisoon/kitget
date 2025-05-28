package com.project.kitgetback.repository;

import com.project.kitgetback.DTO.Item;
import com.project.kitgetback.entity.ShoppingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingRepository extends JpaRepository<ShoppingEntity, Integer> {
    @Query("SELECT DISTINCT s.className FROM ShoppingEntity s WHERE s.school = :schoolCode")
    List<String> findDistinctClassesBySchool(@Param("schoolCode") String schoolCode);

    @Query("SELECT s.needs FROM ShoppingEntity s WHERE s.school = :schoolCode AND s.className = :className")
    List<String> findNeedsBySchoolAndClass(@Param("schoolCode") String schoolCode, @Param("className") String className);

    @Query("SELECT new com.project.kitgetback.DTO.Item(s.needs, s.price) FROM ShoppingEntity s WHERE s.school = :schoolCode AND s.className = :className")
    List<Item> findItemsBySchoolAndClass(@Param("schoolCode") String schoolCode, @Param("className") String className);

}
