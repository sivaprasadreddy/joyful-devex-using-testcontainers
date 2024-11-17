package com.sivalabs.localstackdemo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
    static final String BUCKET_NAME = UUID.randomUUID().toString();
    static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Bean
    @ServiceConnection
    LocalStackContainer localstackContainer(DynamicPropertyRegistry registry) {
        LocalStackContainer localStack =
                new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.8.1"));
        try {
            localStack.start();
            localStack.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
            localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", QUEUE_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //With @ServiceConnection, you don't need to configure these props anymore.
        //registry.add("spring.cloud.aws.credentials.access-key", localStack::getAccessKey);
        //registry.add("spring.cloud.aws.credentials.secret-key", localStack::getSecretKey);
        //registry.add("spring.cloud.aws.region.static", localStack::getRegion);
        //registry.add("spring.cloud.aws.endpoint", localStack::getEndpoint);

        registry.add("app.bucket", () -> BUCKET_NAME);
        registry.add("app.queue", () -> QUEUE_NAME);
        return localStack;
    }
}
