package ru.hogwarts.school.services.api;

import ru.hogwarts.school.model.student.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService extends CRUDService<UUID, Student> {
    Double getAverageAge();
    Integer getCountStudents();
    List<Student> getLastFiveOldStudents();
}
