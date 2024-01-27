package ru.hogwarts.school.model.avatar;

import jakarta.persistence.*;
import ru.hogwarts.school.model.student.Student;

import java.util.UUID;

@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    public Avatar() {
    }

    public Avatar(UUID id, String filePath, long fileSize, String mediaType, byte[] data, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }

    private Avatar(Builder builder) {
        setId(builder.id);
        setFilePath(builder.filePath);
        setFileSize(builder.fileSize);
        setMediaType(builder.mediaType);
        setData(builder.data);
        setStudent(builder.student);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String filePath;
        private long fileSize;
        private String mediaType;
        private byte[] data;
        private Student student;

        public Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder filePath(String val) {
            filePath = val;
            return this;
        }

        public Builder fileSize(long val) {
            fileSize = val;
            return this;
        }

        public Builder mediaType(String val) {
            mediaType = val;
            return this;
        }

        public Builder data(byte[] val) {
            data = val;
            return this;
        }

        public Builder student(Student val) {
            student = val;
            return this;
        }

        public Avatar build() {
            return new Avatar(this);
        }
    }
}
