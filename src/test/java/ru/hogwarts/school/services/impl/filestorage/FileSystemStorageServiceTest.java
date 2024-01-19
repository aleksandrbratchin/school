package ru.hogwarts.school.services.impl.filestorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;
import ru.hogwarts.school.services.impl.filestorage.exception.StorageException;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class FileSystemStorageServiceTest {

    private FileSystemStorageService storageService;

    @BeforeEach
    public void init() {
        storageService = new FileSystemStorageService();
    }

    @Nested
    class Save {
        @BeforeEach
        public void init() {
            FileSystemUtils.deleteRecursively(Path.of("avatar", "test").toFile());
        }

        @Test
        public void save() {
            MockMultipartFile foo = new MockMultipartFile(
                    "foo",
                    "foo.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    "Hello, World".getBytes());
            Path pathFile = Path.of("avatar", "test", foo.getOriginalFilename());
            storageService.save(
                    pathFile,
                    foo
            );

            assertThat(pathFile).exists();
        }

        @Test
        public void saveEmptyFile() {
            MockMultipartFile foo = new MockMultipartFile(
                    "foo",
                    "foo.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    new byte[0]);
            Path pathFile = Path.of("avatar", "test", foo.getOriginalFilename());

            Throwable thrown = catchThrowable(() -> storageService.save(
                            pathFile,
                            foo
                    )
            );

            assertThat(thrown).isInstanceOf(StorageException.class)
                    .hasMessageContaining("Не удалось сохранить пустой файл.");
        }

        @AfterEach
        public void after() {
            FileSystemUtils.deleteRecursively(Path.of("avatar", "test").toFile());
        }
    }

    @Nested
    class ResourceTest {
        @BeforeEach
        public void init() {
            FileSystemUtils.deleteRecursively(Path.of("avatar", "test").toFile());
            MockMultipartFile foo = new MockMultipartFile(
                    "foo",
                    "foo.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    "Hello, World".getBytes());
            Path pathFile = Path.of("avatar", "test", foo.getOriginalFilename());
            storageService.save(
                    pathFile,
                    foo
            );
        }

        @Test
        public void loadAsResource() {
            Resource resource = storageService.loadAsResource(
                    Path.of("avatar", "test", "foo.txt")
            );

            assertThat(resource).isNotNull();
            assertThat(resource.exists()).isTrue();
        }

        @AfterEach
        public void after() {
            FileSystemUtils.deleteRecursively(Path.of("avatar", "test").toFile());
        }
    }

    @Nested
    class Delete {
        @BeforeEach
        public void init() {
            FileSystemUtils.deleteRecursively(Path.of("avatar", "test").toFile());
            Path pathFile = Path.of("avatar", "test", "foo.txt");
            storageService.save(
                    pathFile,
                    new MockMultipartFile(
                            "foo",
                            "foo.txt",
                            MediaType.TEXT_PLAIN_VALUE,
                            "Hello, World".getBytes())
            );
            pathFile = Path.of("avatar", "test", "foo1.txt");
            storageService.save(
                    pathFile,
                    new MockMultipartFile(
                            "foo1",
                            "foo1.txt",
                            MediaType.TEXT_PLAIN_VALUE,
                            "Hello, World".getBytes())
            );
        }

        @Test
        public void loadAsResource() throws IOException {
            Path path = Path.of("avatar", "test");

            storageService.deleteAll(path);

            assertThat(path).doesNotExist();
        }

        @AfterEach
        public void after() {
            FileSystemUtils.deleteRecursively(Path.of("avatar", "test").toFile());
        }
    }

}