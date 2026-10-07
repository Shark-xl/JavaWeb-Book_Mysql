package com.book.mapper;

import com.book.entity.Category;

import java.util.List;

// 商品分类表 category 的数据库访问接口
public interface CategoryMapper {
    // 查询全部分类，用于首页筛选栏和后台商品表单
    List<Category> findAll();

    // 根据分类 id 查询分类名称，用于页面显示当前分类
    Category findById(Integer id);
}
