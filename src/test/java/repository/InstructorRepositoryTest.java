package repository;


import db.ConnectionManager;
import model.Instructor;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import repository.impl.InstructorRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class InstructorRepositoryTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");


    private static ConnectionManager connectionManager;
    private InstructorRepository instructorRepository;

    @BeforeAll
    public static void setUp() {
        mySQLContainer.start();
        connectionManager = new ConnectionManager(
                mySQLContainer.getJdbcUrl(),
                mySQLContainer.getUsername(),
                mySQLContainer.getPassword()
        );
    }

    @AfterAll
    public static void tearDown() {
        mySQLContainer.stop();
    }

    @BeforeEach
    public void setUpEach() {
        instructorRepository = new InstructorRepository(connectionManager);
    }

    @Test
    public void testAddAndGetById() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");

        instructorRepository.add(instructor);

        Instructor retrievedInstructor = instructorRepository.getById(instructor.getId());

        assertNotNull(retrievedInstructor);
        assertEquals(instructor.getName(), retrievedInstructor.getName());
        assertEquals(instructor.getEmail(), retrievedInstructor.getEmail());
    }

    @Test
    public void testGetAll() {
        try {
            clearTable("instructor");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Instructor> instructors = instructorRepository.getAll();

        assertEquals(0, instructors.size());

        Instructor instructor1 = new Instructor();
        instructor1.setName("Аскар");
        instructor1.setEmail("askar@example.com");
        instructorRepository.add(instructor1);

        Instructor instructor2 = new Instructor();
        instructor2.setName("Тимур");
        instructor2.setEmail("timur@example.com");
        instructorRepository.add(instructor2);

        instructors = instructorRepository.getAll();

        assertNotNull(instructors);
        assertEquals(2, instructors.size());
    }

    @Test
    public void testUpdate() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        instructorRepository.add(instructor);

        instructor.setName("Обновленное имя");
        instructor.setEmail("updated.email@example.com");
        instructorRepository.update(instructor);

        Instructor updatedInstructor = instructorRepository.getById(instructor.getId());

        assertNotNull(updatedInstructor);
        assertEquals("Обновленное имя", updatedInstructor.getName());
        assertEquals("updated.email@example.com", updatedInstructor.getEmail());
    }

    @Test
    public void testDelete() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        instructorRepository.add(instructor);

        instructorRepository.delete(instructor.getId());

        Instructor deletedInstructor = instructorRepository.getById(instructor.getId());

        assertNull(deletedInstructor);
    }

    public void clearTable(String tableName) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            if ("instructor".equals(tableName)) {
                statement.executeUpdate("DELETE FROM course");
            }
            statement.executeUpdate("DELETE FROM " + tableName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}