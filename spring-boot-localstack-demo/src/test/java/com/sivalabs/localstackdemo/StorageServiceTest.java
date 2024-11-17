package com.sivalabs.localstackdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class StorageServiceTest extends AbstractIntegrationTest {
    @Autowired
    StorageService storageService;

    @Autowired
    ApplicationProperties properties;

    @Test
    void shouldUploadAndDownloadFromS3() throws IOException {
        String key = UUID.randomUUID().toString();
        String message = "TestMessage-"+System.currentTimeMillis();
        ByteArrayInputStream is = new ByteArrayInputStream(message.getBytes(UTF_8));
        storageService.upload(properties.bucket(), key, is);
        String response = storageService.downloadAsString(properties.bucket(), key);
        assertThat(response).isEqualTo(message);
    }
}