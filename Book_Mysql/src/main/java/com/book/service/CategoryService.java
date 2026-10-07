package com.book.service;

import com.book.entity.Category;
import com.book.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    // 分类 id 为空时返回 null，页面可理解为“全部分类”
    public Category findById(Integer id) {
        if (id == null) {
            return null;
        }
        return categoryMapper.findById(id);
    }
}
