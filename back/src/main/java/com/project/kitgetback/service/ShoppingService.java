package com.project.kitgetback.service;

import com.project.kitgetback.DTO.Item;
import com.project.kitgetback.repository.ShoppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingService {
    @Autowired
    private ShoppingRepository shoppingRepository;

    public List<String> getDistinctClassesBySchool(String schoolCode) {
        return shoppingRepository.findDistinctClassesBySchool(schoolCode);
    }

    public List<String> getNeedsBySchoolAndClass(String schoolCode, String className) {
        return shoppingRepository.findNeedsBySchoolAndClass(schoolCode, className);
    }

    public List<Item> getItemsBySchoolAndClass(String schoolCode, String className) {
        return shoppingRepository.findItemsBySchoolAndClass(schoolCode, className);
    }
}
