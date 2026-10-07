package com.book.service;

import com.book.entity.Product;
import com.book.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<Product> findAll() {
        return productMapper.findAll();
    }

    // categoryId 为空表示查询全部商品，否则按分类查询
    public List<Product> findByCategoryId(Integer categoryId) {
        if (categoryId == null) {
            return findAll();
        }
        return productMapper.findByCategoryId(categoryId);
    }

    public Product findById(Integer id) {
        return productMapper.findById(id);
    }

    // 商品 id 为空表示新增；id 不为空表示修改
    public void save(Product product) {
        normalize(product);
        if (product.getId() == null) {
            if (product.getSales() == null) {
                product.setSales(0);
            }
            productMapper.insert(product);
        } else {
            productMapper.update(product);
        }
    }

    // 删除前检查 order_item 是否有历史订单引用，避免订单明细变成无效数据
    public String delete(Integer id) {
        if (productMapper.countOrderReferences(id) > 0) {
            return "该商品已经出现在订单中，不能直接删除";
        }
        productMapper.delete(id);
        return null;
    }

    // 给商品字段设置默认值，避免空值写入数据库后影响页面展示或计算
    private void normalize(Product product) {
        if (!StringUtils.hasText(product.getImageUrl())) {
            product.setImageUrl("");
        }
        if (product.getStock() == null) {
            product.setStock(0);
        }
        if (product.getSales() == null) {
            product.setSales(0);
        }
    }
}
