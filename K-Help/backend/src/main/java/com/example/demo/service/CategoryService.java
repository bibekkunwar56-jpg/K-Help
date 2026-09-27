package com.example.demo.service;

import com.example.demo.dto.CategoryResponse;
import com.example.demo.repository.CategoryRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(category -> category.getName()))
                .map(CategoryResponse::from)
                .toList();
    }
}
