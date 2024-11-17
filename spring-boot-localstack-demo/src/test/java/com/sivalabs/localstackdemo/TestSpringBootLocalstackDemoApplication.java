package com.sivalabs.localstackdemo;


import org.springframework.boot.SpringApplication;

public class TestSpringBootLocalstackDemoApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringBootLocalstackDemoApplication::main)
                .with(TestcontainersConfig.class)
                .run(args);
    }
}
