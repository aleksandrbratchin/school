package ru.hogwarts.school.model.faculty;


import jakarta.persistence.*;
import ru.hogwarts.school.model.student.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String color;

    @OneToMany(
            mappedBy = "faculty",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Student> students = new ArrayList<>();

    public Faculty() {
    }

    public Faculty(UUID id, String name, String color, List<Student> students) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.students = students;
    }

    public Faculty(String name, String color, List<Student> students) {
        this.name = name;
        this.color = color;
        this.students = students;
    }

    private Faculty(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setColor(builder.color);
        setStudents(builder.students);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String name;
        private String color;
        private List<Student> students;

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

        public Builder color(String val) {
            color = val;
            return this;
        }

        public Builder students(List<Student> val) {
            students = val;
            return this;
        }

        public Faculty build() {
            return new Faculty(this);
        }
    }
}
