package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FacultyControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FacultyController facultyController;
    @MockBean
    private FacultyRepository repository;

    @Test
    void contextLoad() {
        assertThat(facultyController).isNotNull();
    }

    @Test
    void delete() throws Exception {
        Faculty ravenclaw = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Когтевран", "синий, бронзовый", new ArrayList<>());
        Mockito.when(repository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(ravenclaw));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculty/{id}", "3fa85f64-5717-4562-b3fc-2c963f66afa6")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .andExpect(jsonPath("$.name").value("Когтевран"))
                .andExpect(jsonPath("$.color").value("синий, бронзовый"));
    }

    @Test
    void add() throws Exception {
        JSONObject slytherinAdd = new JSONObject();
        slytherinAdd.put("name", "Слизерин");
        slytherinAdd.put("color", "зелёный, серебряный");
        Faculty newSlytherin = new Faculty(UUID.randomUUID(), "Слизерин", "зелёный, серебряный", new ArrayList<>());
        Mockito.when(repository.exists(any(Specification.class)))
                .thenReturn(false);
        Mockito.when(repository.save(any(Faculty.class)))
                .thenReturn(newSlytherin);

        mockMvc.perform(
                        post("/faculty")
                                .content(slytherinAdd.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newSlytherin.getId().toString()))
                .andExpect(jsonPath("$.name").value(newSlytherin.getName()))
                .andExpect(jsonPath("$.color").value(newSlytherin.getColor()));
    }

    @Test
    void update() throws Exception {
        JSONObject slytherinJsonObject = new JSONObject(
                """
                        {
                          "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                          "name": "Слизерин",
                          "color": "зелёный, серебряный"
                        }
                        """
        );

        Faculty ravenclaw = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Когтевран", "синий, бронзовый", new ArrayList<>());
        Faculty slytherin = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Слизерин", "зелёный, серебряный", new ArrayList<>());
        Mockito.when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(ravenclaw));
        Mockito.when(repository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(ravenclaw));
        Mockito.when(repository.exists(any(Specification.class)))
                .thenReturn(false);
        Mockito.when(repository.save(any(Faculty.class)))
                .thenReturn(slytherin);

        mockMvc.perform(
                        put("/faculty")
                                .content(slytherinJsonObject.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(slytherin.getId().toString()))
                .andExpect(jsonPath("$.name").value(slytherin.getName()))
                .andExpect(jsonPath("$.color").value(slytherin.getColor()));
    }

    @Test
    void getAll() throws Exception {
        List<Faculty> faculties = new ArrayList<>();
        Faculty gryffindor = new Faculty(UUID.randomUUID(), "Гриффиндор", "алый, золотой", new ArrayList<>());
        Faculty ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый", new ArrayList<>());
        Faculty hufflepuff = new Faculty(UUID.randomUUID(), "Пуффендуй", "канареечно-жёлтый, чёрный", new ArrayList<>());
        Faculty slytherin = new Faculty(UUID.randomUUID(), "Слизерин", "зелёный, серебряный", new ArrayList<>());
        faculties.add(gryffindor);
        faculties.add(ravenclaw);
        faculties.add(hufflepuff);
        faculties.add(slytherin);
        Mockito.when(repository.findAll())
                .thenReturn(faculties);

        mockMvc.perform(
                        get("/faculty/All")
                        //.contentType(MediaType.APPLICATION_JSON)
                        //.accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    void filterByColor() throws Exception {
        Faculty ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый", new ArrayList<>());
        Mockito.when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(ravenclaw));

        mockMvc.perform(
                        get("/faculty/filterByColor")
                                .param("color", "синий")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void students() throws Exception {
        Student granger = new Student(UUID.randomUUID(), "Гермиона Джин Грейнджер", 11, null);
        Student potter = new Student(UUID.randomUUID(), "Гарри Джеймс Поттер", 11, null);
        List<Student> students = Arrays.asList(granger, potter);
        Faculty ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый", students);
        Mockito.when(repository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(ravenclaw));

        mockMvc.perform(
                        get("/faculty/{id}/students", "3fa85f64-5717-4562-b3fc-2c963f66afa6")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    void findByNameOrColor() throws Exception {
        Faculty ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый", new ArrayList<>());
        Mockito.when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(ravenclaw));

        mockMvc.perform(
                        get("/faculty/findByNameOrColor")
                                .param("name", "Когтевран")
                                .param("color", "синий")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

}