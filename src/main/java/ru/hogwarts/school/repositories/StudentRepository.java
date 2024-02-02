package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.student.Student;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student> {
    @Query(
            value = "SELECT AVG(age) age FROM student",
            nativeQuery = true
    )
    Double getAverageAge();

    @Query(
            value = "SELECT count(*) FROM student",
            nativeQuery = true
    )
    Integer getCountStudents();

    @Query(value = """
            SELECT id, age, name, faculty_id
            FROM public.student
            order by age desc limit (5);
            """,
            nativeQuery = true
    )
    List<Student> getLastFiveOldStudents();
}
