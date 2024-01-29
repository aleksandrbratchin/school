package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import ru.hogwarts.school.dto.faculty.FacultyResponseDto;
import ru.hogwarts.school.dto.student.StudentResponseDto;
import ru.hogwarts.school.model.faculty.Faculty;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentController studentController;
    @MockBean
    private StudentRepository repository;

    @MockBean
    private FacultyRepository facultyRepository;

    @Test
    void contextLoad() {
        assertThat(studentController).isNotNull();
    }

    @Test
    void delete() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", "3fa85f64-5717-4562-b3fc-2c963f66afa6");
        Student potter = new Student(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Гарри Джеймс Поттер", 11, null);

        Mockito.when(repository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(potter));

        ResponseEntity<StudentResponseDto> response = restTemplate.exchange(
                "/student/{id}",
                HttpMethod.DELETE,
                null,
                StudentResponseDto.class,
                uriVariables
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().id().toString()).isEqualTo("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        assertThat(response.getBody().name()).isEqualTo("Гарри Джеймс Поттер");
        assertThat(response.getBody().age()).isEqualTo(11);
    }

    @Test
    void add() throws JSONException {
        Student potter = new Student(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Гарри Джеймс Поттер", 11, null);
        JSONObject potterAdd = new JSONObject();
        potterAdd.put("name", "Гарри Джеймс Поттер");
        potterAdd.put("age", "11");
        Mockito.when(repository.save(any(Student.class)))
                .thenReturn(potter);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =
                new HttpEntity<>(potterAdd.toString(), headers);

        ResponseEntity<StudentResponseDto> response = restTemplate.
                postForEntity(
                        "/student",
                        request,
                        StudentResponseDto.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().id().toString()).isEqualTo("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        assertThat(response.getBody().name()).isEqualTo("Гарри Джеймс Поттер");
        assertThat(response.getBody().age()).isEqualTo(11);
    }

    @Test
    void update() throws JSONException {
        Student potter = new Student(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Рон Уизли", 11, null);
        JSONObject potterAdd = new JSONObject();
        potterAdd.put("id", "3fa85f64-5717-4562-b3fc-2c963f66afa6");
        potterAdd.put("name", "Гарри Джеймс Поттер");
        potterAdd.put("age", "11");
        Mockito.when(repository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(potter));
        Mockito.when(repository.existsById(any(UUID.class)))
                .thenReturn(true);
        Mockito.when(repository.save(any(Student.class)))
                .thenReturn(potter);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =
                new HttpEntity<>(potterAdd.toString(), headers);

        ResponseEntity<StudentResponseDto> response = restTemplate.
                exchange(
                        "/student",
                        HttpMethod.PUT,
                        request,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().id().toString()).isEqualTo("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        assertThat(response.getBody().name()).isEqualTo("Гарри Джеймс Поттер");
        assertThat(response.getBody().age()).isEqualTo(11);
    }

    @Test
    void getAll() {
        Faculty gryffindor = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Гриффиндор", "алый, золотой", new ArrayList<>());
        List<Student> students;
        Student granger = new Student(UUID.randomUUID(), "Гермиона Джин Грейнджер", 11, gryffindor);
        Student malfoy = new Student(UUID.randomUUID(), "Драко Люциус Малфой", 12, gryffindor);
        Student lovegood = new Student(UUID.randomUUID(), "Полумна Лавгуд", 11, gryffindor);
        students = List.of(granger, malfoy, lovegood);
        Mockito.when(repository.findAll())
                .thenReturn(students);
        Mockito.when(facultyRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(gryffindor));

        ResponseEntity<List<StudentResponseDto>> response = restTemplate.exchange(
                "/student/All",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getBody().size()).isEqualTo(3);
    }

    @Test
    void filterByAge() {
        Faculty gryffindor = new Faculty(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Гриффиндор", "алый, золотой", new ArrayList<>());
        List<Student> students;
        Student granger = new Student(UUID.randomUUID(), "Гермиона Джин Грейнджер", 12, gryffindor);
        Student lovegood = new Student(UUID.randomUUID(), "Полумна Лавгуд", 12, gryffindor);
        students = List.of(granger, lovegood);
        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("age", 12);
        Mockito.when(repository.findAll(any(Specification.class)))
                .thenReturn(students);
        Mockito.when(facultyRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(gryffindor));

        ResponseEntity<List<StudentResponseDto>> response = restTemplate.exchange(
                "/student/filterByAge?age={age}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                uriVariables
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void faculty() {
        Faculty ravenclaw = new Faculty(UUID.randomUUID(), "Когтевран", "синий, бронзовый", new ArrayList<>());
        Student granger = new Student(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Гермиона Джин Грейнджер", 11, ravenclaw);
        Mockito.when(repository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(granger));
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", "3fa85f64-5717-4562-b3fc-2c963f66afa6");

        ResponseEntity<FacultyResponseDto> response = restTemplate.exchange(
                "/student/{id}/faculty",
                HttpMethod.GET,
                null,
                FacultyResponseDto.class,
                uriVariables
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Когтевран");
        assertThat(response.getBody().color()).isEqualTo("синий, бронзовый");
    }

    @Test
    void findByAgeBetween() {
        Student potter = new Student(UUID.randomUUID(), "Гарри Джеймс Поттер", 11, null);
        Student lovegood = new Student(UUID.randomUUID(), "Полумна Лавгуд", 11, null);
        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("min", 10);
        uriVariables.put("max", 11);
        Mockito.when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(potter, lovegood));

        ResponseEntity<List<StudentResponseDto>> response = restTemplate.exchange(
                "/student/findByAgeBetween?min={min}&max={max}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                uriVariables
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }
}