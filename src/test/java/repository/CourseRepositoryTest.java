package repository;

import db.ConnectionManager;
import model.Course;
import model.Instructor;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import repository.impl.CourseRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class CourseRepositoryTest {

//    private ConnectionManager connectionManager = new ConnectionManager();
//    CourseRepository courseRepository = new CourseRepository(connectionManager);

    // Запуск контейнера с тестовой базой данных
    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest").withDatabaseName("course").withUsername("root").withPassword("askar2983387");

    @BeforeAll
    public static void setUp() {
        mySQLContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mySQLContainer.stop();
    }

    @BeforeEach
    public void init() throws SQLException {
        MockitoAnnotations.initMocks(this);
        // Подключение к контейнеру с тестовой базой данных
//        connectionManager = mySQLContainer.createConnection("");
//        courseRepository = new CourseRepository(mockConnection, connectionManager);
    }

 /*   @Test
    public void testAddCourse() {
        String url = mySQLContainer.getJdbcUrl();
        try {
            connectionManager.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Description");

        Assertions.assertDoesNotThrow(() -> {
            courseRepository.add(course);
        });

        Assertions.assertNotNull(course.getId()); // Проверка, что id был сгенерирован
    }*/

    //@Test
   /* public void testGetCourseById() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Test Course");
        course.setDescription("Test Description");

        Mockito.when(courseRepository.getById(Mockito.anyInt())).thenReturn(course);

        Course retrievedCourse = courseRepository.getById(1);

        Assertions.assertNotNull(retrievedCourse);
        assertEquals(course.getId(), retrievedCourse.getId());
        assertEquals(course.getTitle(), retrievedCourse.getTitle());
        assertEquals(course.getDescription(), retrievedCourse.getDescription());
    }
   */
    @Test
    void testGetById() {
        // Set up ConnectionManager
        ConnectionManager connectionManager = new ConnectionManager(
                mySQLContainer.getJdbcUrl(),
                mySQLContainer.getUsername(),
                mySQLContainer.getPassword()
        );

        // Create CourseRepository
        CourseRepository courseRepository = new CourseRepository(connectionManager);

        // Add a course
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("This is a test course");
        course.setInstructor(new Instructor());
        courseRepository.add(course);

        // Get the course by ID
        Course retrievedCourse = courseRepository.getById(course.getId());

        // Assert that the retrieved course is the same as the added course
        assertEquals(course.getId(), retrievedCourse.getId());
        assertEquals(course.getTitle(), retrievedCourse.getTitle());
        assertEquals(course.getDescription(), retrievedCourse.getDescription());
    }

    //@Test
    /*public void testUpdateCourse() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Test Course");
        course.setDescription("Test Description");

        Assertions.assertDoesNotThrow(() -> {
            courseRepository.update(course);
        });
    }

    @Test
    public void testDeleteCourse() {
        Assertions.assertDoesNotThrow(() -> {
            courseRepository.delete(1);
        });
    }*/
}