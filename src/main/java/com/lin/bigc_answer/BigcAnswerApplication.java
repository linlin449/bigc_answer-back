package com.lin.bigc_answer;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
public class BigcAnswerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BigcAnswerApplication.class, args);
    }
}
