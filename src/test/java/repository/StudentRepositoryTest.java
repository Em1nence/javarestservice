package repository;

import db.ConnectionManager;
import model.Student;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import repository.impl.StudentRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class StudentRepositoryTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    private static ConnectionManager connectionManager;
    private StudentRepository studentRepository;

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
        studentRepository = new StudentRepository(connectionManager);
    }

    @Test
    public void testAddAndGetById() {
        Student student = new Student();
        student.setName("Карл Маркс");
        student.setEmail("red@example.com");

        studentRepository.addStudent(student);

        assertNotNull(student.getId());

        Student retrievedStudent = studentRepository.getStudentById(student.getId());

        assertNotNull(retrievedStudent);
        assertEquals(student.getName(), retrievedStudent.getName());
        assertEquals(student.getEmail(), retrievedStudent.getEmail());
        assertEquals(student.getId(), retrievedStudent.getId());
    }

    @Test
    public void testGetAll() {
        try {
            clearTable("student");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Student> students = studentRepository.getAllStudents();

        assertEquals(0, students.size());

        Student student1 = new Student();
        student1.setName("Карл Маркс");
        student1.setEmail("red@example.com");
        studentRepository.addStudent(student1);

        Student student2 = new Student();
        student2.setName("Василий Уткин");
        student2.setEmail("ytk@example.com");
        studentRepository.addStudent(student2);

        students = studentRepository.getAllStudents();

        assertNotNull(students);
        assertEquals(2, students.size());
    }


    @Test
    public void testDelete() {
        Student student = new Student();
        student.setName("Карл Маркс");
        student.setEmail("red@example.com");
        studentRepository.addStudent(student);

        studentRepository.deleteStudent(student.getId());

        Student deletedStudent = studentRepository.getStudentById(student.getId());

        assertNull(deletedStudent);
    }

    public void clearTable(String tableName) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + tableName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
