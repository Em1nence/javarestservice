package repository;

import db.ConnectionManager;
import model.Course;
import model.Instructor;
import model.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
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

@Testcontainers
public class CourseRepositoryTest {
    private ConnectionManager connectionManager;
    private CourseRepository courseRepository;

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("testDb")
            .withUsername("test-user")
            .withPassword("123")
            .withInitScript("create_tables.sql");

    @BeforeAll
    public static void setUp() {
        mySQLContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mySQLContainer.stop();
    }

    @BeforeEach
    public void setUpEach() {
        connectionManager = new ConnectionManager();
        connectionManager.setCredentials(
                mySQLContainer.getJdbcUrl(),
                mySQLContainer.getUsername(),
                mySQLContainer.getPassword()
        );
        courseRepository = new CourseRepository(connectionManager);
    }

    @Test
    public void testAddAndGetById() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.addInstructor(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);

        courseRepository.addCourse(course);

        assertNotNull(course.getId());

        Course retrievedCourse = courseRepository.getCourseById(course.getId());

        assertNotNull(retrievedCourse);
        assertEquals(course.getTitle(), retrievedCourse.getTitle());
        assertEquals(course.getDescription(), retrievedCourse.getDescription());
        assertNotNull(retrievedCourse.getInstructor());
        assertEquals(instructor.getId(), retrievedCourse.getInstructor().getId());
    }

    @Test
    public void testGetAll() {
        try {
            clearTable("Instructor");
            clearTable("Course");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Course> courses = courseRepository.getAllCourses();

        assertEquals(0, courses.size());

        Instructor instructor1 = new Instructor();
        instructor1.setName("Аскар");
        instructor1.setEmail("john.doe@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.addInstructor(instructor1);

        Course course1 = new Course();
        course1.setTitle("Java Programming");
        course1.setDescription("Learn Java programming");
        course1.setInstructor(instructor1);
        courseRepository.addCourse(course1);

        Instructor instructor2 = new Instructor();
        instructor2.setName("Алиса");
        instructor2.setEmail("alise@example.com");
        instructorRepository.addInstructor(instructor2);

        Course course2 = new Course();
        course2.setTitle("Database Design");
        course2.setDescription("Learn database design");
        course2.setInstructor(instructor2);
        courseRepository.addCourse(course2);

        courses = courseRepository.getAllCourses();

        assertNotNull(courses);
        assertEquals(2, courses.size());
    }

    @Test
    public void testUpdate() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.addInstructor(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.addCourse(course);

        course.setTitle("Updated Title");
        course.setDescription("Updated description");
        courseRepository.updateCourse(course);

        Course updatedCourse = courseRepository.getCourseById(course.getId());

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
        instructorRepository.addInstructor(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.addCourse(course);

        courseRepository.deleteCourse(course.getId());

        Course deletedCourse = courseRepository.getCourseById(course.getId());

        assertNull(deletedCourse);
    }

    @Test
    public void testEnrollStudentInCourse() {
        Instructor instructor = new Instructor();
        instructor.setName("Аскар");
        instructor.setEmail("askar@example.com");
        InstructorRepository instructorRepository = new InstructorRepository(connectionManager);
        instructorRepository.addInstructor(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.addCourse(course);

        Student student = new Student();
        student.setName("Алиса");
        student.setEmail("alice@example.com");
        StudentRepository studentRepository = new StudentRepository(connectionManager);
        studentRepository.addStudent(student);

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
        instructorRepository.addInstructor(instructor);

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java programming");
        course.setInstructor(instructor);
        courseRepository.addCourse(course);

        Student student = new Student();
        student.setName("Алиса");
        student.setEmail("alice@example.com");
        StudentRepository studentRepository = new StudentRepository(connectionManager);
        studentRepository.addStudent(student);

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
            if ("Instructor".equals(tableName)) {
                statement.executeUpdate("DELETE FROM Course");
            }
            // Очистить указанную таблицу
            statement.executeUpdate("DELETE FROM " + tableName);
        }
    }
}