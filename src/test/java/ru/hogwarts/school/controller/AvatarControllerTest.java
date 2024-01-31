package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.repositories.AvatarRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AvatarControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AvatarController avatarController;
    @MockBean
    private AvatarRepository repository;

    @Test
    void all() throws Exception {
        Page mock = mock(Page.class);
        Mockito.when(repository.findAll(any(PageRequest.class)))
                .thenReturn(mock);

        mockMvc.perform(
                        get("/student/avatar/All")
                                .param("page", "1")
                                .param("size", "5")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}