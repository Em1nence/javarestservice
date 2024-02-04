package repository;

import db.ConnectionManager;
import model.Course;
import model.Instructor;
import model.Student;
import org.junit.jupiter.api.*;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import repository.impl.CourseRepository;
import repository.impl.InstructorRepository;
import repository.impl.StudentRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Testcontainers
public class CourseRepositoryTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    private static ConnectionManager connectionManager;
    private CourseRepository courseRepository;

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
        courseRepository = new CourseRepository(connectionManager);
    }

    @Test
    public void testAddAndGetById() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.add(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);

        courseRepository.add(course);

        assertNotNull(course.getId());

        Course retrievedCourse = courseRepository.getById(course.getId());

        assertNotNull(retrievedCourse);
        assertEquals(course.getTitle(), retrievedCourse.getTitle());
        assertEquals(course.getDescription(), retrievedCourse.getDescription());
        assertNotNull(retrievedCourse.getInstructor());
        assertEquals(instructor.getId(), retrievedCourse.getInstructor().getId());
    }

    @Test
    public void testGetAll() {
        try {
            clearTable("instructor");
            clearTable("course");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Course> courses = courseRepository.getAll();

        assertEquals(0, courses.size());

        Instructor instructor1 = new Instructor();
        instructor1.setName("Аскар");
        instructor1.setEmail("john.doe@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.add(instructor1);

        Course course1 = new Course();
        course1.setTitle("Java Programming");
        course1.setDescription("Learn Java programming");
        course1.setInstructor(instructor1);
        courseRepository.add(course1);

        Instructor instructor2 = new Instructor();
        instructor2.setName("Алиса");
        instructor2.setEmail("alise@example.com");
        instructorRepository.add(instructor2);

        Course course2 = new Course();
        course2.setTitle("Database Design");
        course2.setDescription("Learn database design");
        course2.setInstructor(instructor2);
        courseRepository.add(course2);

        courses = courseRepository.getAll();

        assertNotNull(courses);
        assertEquals(2, courses.size());
    }

    @Test
    public void testUpdate() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.add(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.add(course);

        course.setTitle("Updated Title");
        course.setDescription("Updated description");
        courseRepository.update(course);

        Course updatedCourse = courseRepository.getById(course.getId());

        assertNotNull(updatedCourse);
        assertEquals("Updated Title", updatedCourse.getTitle());
        assertEquals("Updated description", updatedCourse.getDescription());
    }

    @Test
    public void testDelete() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.add(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.add(course);

        courseRepository.delete(course.getId());

        Course deletedCourse = courseRepository.getById(course.getId());

        assertNull(deletedCourse);
    }

    @Test
    public void testEnrollStudentInCourse() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.add(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.add(course);

        Student student = new Student();
        student.setName("Алиса");
        student.setEmail("alice@example.com");
        StudentRepository studentRepository = new StudentRepository(connectionManager);
        studentRepository.add(student);

        courseRepository.enrollStudentInCourse(course.getId(), student.getId());

        List<Student> enrolledStudents = studentRepository.getStudentsByCourseId(course.getId());

        assertNotNull(enrolledStudents);
        assertEquals(1, enrolledStudents.size());
        assertEquals(student.getId(), enrolledStudents.get(0).getId());
    }


    @Test
    public void testRemoveStudentFromCourse() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.add(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.add(course);

        Student student = new Student();
        student.setName("Алиса");
        student.setEmail("alice@example.com");
        StudentRepository studentRepository = new StudentRepository(connectionManager);
        studentRepository.add(student);

        courseRepository.enrollStudentInCourse(course.getId(), student.getId());

        List<Student> enrolledStudentsBeforeRemoval = studentRepository.getStudentsByCourseId(course.getId());
        assertNotNull(enrolledStudentsBeforeRemoval);
        assertEquals(1, enrolledStudentsBeforeRemoval.size());

        courseRepository.removeStudentFromCourse(course.getId(), student.getId());

        List<Student> enrolledStudentsAfterRemoval = studentRepository.getStudentsByCourseId(course.getId());

        assertNotNull(enrolledStudentsAfterRemoval);
        assertEquals(0, enrolledStudentsAfterRemoval.size());
    }




    public void clearTable(String tableName) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            // Удалить записи в связанных таблицах перед очисткой
            if ("instructor".equals(tableName)) {
                statement.executeUpdate("DELETE FROM course");
            }
            // Очистить указанную таблицу
            statement.executeUpdate("DELETE FROM " + tableName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}