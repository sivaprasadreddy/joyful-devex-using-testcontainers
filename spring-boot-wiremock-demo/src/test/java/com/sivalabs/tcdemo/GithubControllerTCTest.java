package com.sivalabs.tcdemo;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = WireMockContainerInitializer.class)
@AutoConfigureMockMvc
public class GithubControllerTCTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @Test
    void shouldGetGithubUserProfile() throws Exception {
        String username = "sivaprasadreddy";
        this.mockMvc
                .perform(get("/api/users/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(username)))
                .andExpect(jsonPath("$.name", is("K. Siva Prasad Reddy")))
                .andExpect(jsonPath("$.public_repos", is(50)));
    }
}
