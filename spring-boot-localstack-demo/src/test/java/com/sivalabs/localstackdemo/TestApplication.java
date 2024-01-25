package com.sivalabs.localstackdemo;


import org.springframework.boot.SpringApplication;

public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringBootLocalstackDemoApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
