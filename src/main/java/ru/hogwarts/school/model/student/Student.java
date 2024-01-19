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
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Student() {
    }

    public Student(UUID id, String name, int age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }

    public Student(String name, int age, Faculty faculty) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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
        private int age;
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

        public Builder age(int val) {
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
}
