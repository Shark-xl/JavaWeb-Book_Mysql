package com.book.controller;

import com.book.entity.Product;
import com.book.entity.User;
import com.book.service.CategoryService;
import com.book.service.ImageService;
import com.book.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    public AdminController(ProductService productService, CategoryService categoryService, ImageService imageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {
        // 后台页面只允许 role=1 的管理员访问
        if (!isAdmin(session)) {
            return "redirect:/index";
        }
        model.addAttribute("products", productService.findAll());
        return "admin/index";
    }

    // 打开新增商品页面，需要同时查询分类表用于下拉选择
    @GetMapping("/admin/products/new")
    public String newProduct(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/index";
        }
        model.addAttribute("product", new Product());
        model.addAttribute("actionUrl", "/admin/products");
        fillForm(model);
        return "admin/product-form";
    }

    // 新增商品：图片路径处理完成后写入 product 表
    @PostMapping("/admin/products")
    public String create(Product product,
                         @RequestParam(value = "selectedImageUrl", required = false) String selectedImageUrl,
                         @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) throws IOException {
        if (!isAdmin(session)) {
            return "redirect:/index";
        }
        applyImage(product, selectedImageUrl, uploadFile);
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "商品新增成功");
        return "redirect:/admin";
    }

    // 打开商品编辑页面：根据 id 查询 product 表，回显原商品信息
    @GetMapping("/admin/products/{id}/edit")
    public String edit(@PathVariable Integer id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/index";
        }
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("actionUrl", "/admin/products/" + id);
        fillForm(model);
        return "admin/product-form";
    }

    // 修改商品：根据路径中的 id 更新 product 表
    @PostMapping("/admin/products/{id}")
    public String update(@PathVariable Integer id,
                         Product product,
                         @RequestParam(value = "selectedImageUrl", required = false) String selectedImageUrl,
                         @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) throws IOException {
        if (!isAdmin(session)) {
            return "redirect:/index";
        }
        product.setId(id);
        applyImage(product, selectedImageUrl, uploadFile);
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "商品修改成功");
        return "redirect:/admin";
    }

    // 删除商品：Service 会先检查 order_item 是否引用该商品
    @PostMapping("/admin/products/{id}/delete")
    public String delete(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/index";
        }
        String error = productService.delete(id);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
        } else {
            redirectAttributes.addFlashAttribute("message", "商品删除成功");
        }
        return "redirect:/admin";
    }

    // 单独上传图片，返回路径后可作为 product.image_url 保存
    @PostMapping("/admin/images/upload")
    public String uploadOnly(@RequestParam("uploadFile") MultipartFile uploadFile,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) throws IOException {
        if (!isAdmin(session)) {
            return "redirect:/index";
        }
        String path = imageService.upload(uploadFile);
        redirectAttributes.addFlashAttribute("message", "图片上传成功：" + path);
        return "redirect:/admin/products/new";
    }

    private void fillForm(Model model) {
        model.addAttribute("categories", categoryService.findAll());
    }

    // 优先使用页面选中的图片；如果上传了新图片，则新上传图片覆盖原路径
    private void applyImage(Product product, String selectedImageUrl, MultipartFile uploadFile) throws IOException {
        if (selectedImageUrl != null && !selectedImageUrl.isBlank()) {
            product.setImageUrl(selectedImageUrl);
        }
        String uploaded = imageService.upload(uploadFile);
        if (!uploaded.isBlank()) {
            product.setImageUrl(uploaded);
        }
    }

    // 从 Session 中读取当前用户，并判断 role 是否为管理员
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currUser");
        return user != null && user.getRole() != null && user.getRole() == 1;
    }
}
