package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("port8082")
class InfoControllerPort8082Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InfoController infoController;

    @Nested
    class ProfileWorkPort8082 {
        @Test
        void getPort() throws Exception {
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get("/student/port"))
                    .andExpect(status().isOk())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();

            assertThat(response.getContentAsString()).isEqualTo("8082");
        }
    }

}