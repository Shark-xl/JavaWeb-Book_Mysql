package com.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 扫描 com.book.mapper 下的 MyBatis Mapper 接口，让接口和 XML SQL 绑定成数据库访问对象
@MapperScan("com.book.mapper")
// Spring Boot 启动入口，会自动扫描 controller、service、config 等组件
@SpringBootApplication
public class BookMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMysqlApplication.class, args);
    }
}
