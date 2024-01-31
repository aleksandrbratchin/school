package ru.hogwarts.school.services.api;

import ru.hogwarts.school.dto.student.AverageAgeOfStudents;
import ru.hogwarts.school.dto.student.NumberOfStudents;
import ru.hogwarts.school.model.student.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService extends CRUDService<UUID, Student> {
    AverageAgeOfStudents getAverageAge();
    NumberOfStudents getCountStudents();
    List<Student> getLastFiveOldStudents();
}
