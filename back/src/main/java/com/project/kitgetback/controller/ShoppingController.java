package com.project.kitgetback.controller;

import com.project.kitgetback.DTO.Item;
import com.project.kitgetback.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    @GetMapping("/shopping/{schoolCode}")
    public List<String> getClasses(@PathVariable String schoolCode) {
        return shoppingService.getDistinctClassesBySchool(schoolCode);
    }

    @GetMapping("/shopping/{schoolCode}/{className}/needs")
    public List<String> getNeeds(@PathVariable String schoolCode, @PathVariable String className) {
        return shoppingService.getNeedsBySchoolAndClass(schoolCode, className);
    }

    @GetMapping("/shopping/{schoolCode}/{className}/items")
    public List<Item> getItems(@PathVariable String schoolCode, @PathVariable String className) {
        return shoppingService.getItemsBySchoolAndClass(schoolCode, className);
    }

}
