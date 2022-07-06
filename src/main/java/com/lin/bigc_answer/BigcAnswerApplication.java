package com.lin.bigc_answer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.lin.bigc_answer.mapper")
//@ServletComponentScan
public class BigcAnswerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BigcAnswerApplication.class, args);
    }
}
