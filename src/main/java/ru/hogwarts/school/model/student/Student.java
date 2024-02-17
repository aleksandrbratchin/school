package ru.hogwarts.school.model.student;

import jakarta.persistence.*;
import ru.hogwarts.school.model.faculty.Faculty;

import java.util.UUID;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Student() {
    }

    public Student(UUID id, String name, Integer age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }

    public Student(String name, Integer age, Faculty faculty) {
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }

    private Student(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setAge(builder.age);
        setFaculty(builder.faculty);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public static Student.Builder builder() {
        return new Student.Builder();
    }

    public static final class Builder {
        private UUID id;
        private String name;
        private Integer age;
        private Faculty faculty;

        public Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder age(Integer val) {
            age = val;
            return this;
        }

        public Builder faculty(Faculty val) {
            faculty = val;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", faculty=" + (faculty == null ? "" : faculty.getName()) +
                '}';
    }
}
