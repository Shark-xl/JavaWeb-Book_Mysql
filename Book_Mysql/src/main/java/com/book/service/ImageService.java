package com.book.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageService {
    private final Path uploadRoot;

    public ImageService(@Value("${bookstore.upload-dir}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir);
    }

    public List<String> builtInImages() {
        Path root = Paths.get("src/main/resources/static/images/books");
        if (!Files.isDirectory(root)) {
            return List.of();
        }
        try (Stream<Path> paths = Files.list(root)) {
            return paths.filter(Files::isRegularFile)
                    .filter(this::isImage)
                    .map(path -> "/images/books/" + path.getFileName())
                    .toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    // 保存上传图片，并返回可以写入 product.image_url 的访问路径
    public String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return "";
        }
        Files.createDirectories(uploadRoot);
        String original = file.getOriginalFilename() == null ? "book.jpg" : file.getOriginalFilename();
        String suffix = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            suffix = original.substring(dot);
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + suffix;
        file.transferTo(uploadRoot.resolve(filename));
        return "/uploads/books/" + filename;
    }

    // 只允许常见图片格式出现在图片选择列表中
    private boolean isImage(Path path) {
        String filename = path.getFileName().toString().toLowerCase();
        return filename.endsWith(".jpg")
                || filename.endsWith(".jpeg")
                || filename.endsWith(".png")
                || filename.endsWith(".gif")
                || filename.endsWith(".webp");
    }
}
