package com.springboot.seimi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by wxl on 2019/9/3.
 */
@MapperScan("com.springboot.dao")
@SpringBootApplication
public class SeimiCrawlerApplication {
    public static void main(String[] args){
        SpringApplication.run(SeimiCrawlerApplication.class, args);
    }
}
