package service;

import model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.impl.CourseRepository;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourses() {
        // Создание мока для метода repository
        when(courseRepository.getAllCourses()).thenReturn(new ArrayList<>());

        // Вызов метода getAllCourses
        List<Course> courses = courseService.getAllCourses();

        // Проверка, что список не пустой
        assertNotNull(courses);

        // Проверка, что метод repository был вызван ровно один раз
        verify(courseRepository, times(1)).getAllCourses();
    }

    @Test
    void testAddCourse() {
        // Создание нового курса
        Course newCourse = new Course();
        newCourse.setTitle("New Course");
        newCourse.setDescription("Description");

        // Задаем поведение для мока repository
        when(courseRepository.getCourseById(newCourse.getId())).thenReturn(null);
        doNothing().when(courseRepository).addCourse(newCourse);

        // Вызов метода addCourse
        courseService.addCourse(newCourse);

        // Проверка, что метод repository был вызван ровно один раз
        verify(courseRepository, times(1)).addCourse(newCourse);
    }
    @Test
    void testGetCourseById() {
        int courseId = 1;
        Course expectedCourse = new Course();
        expectedCourse.setId(courseId);
        when(courseRepository.getCourseById(courseId)).thenReturn(expectedCourse);
        Course actualCourse = courseService.getCourseById(courseId);
        assertNotNull(actualCourse);
        assertEquals(expectedCourse, actualCourse);
        verify(courseRepository, times(1)).getCourseById(courseId);
    }

    @Test
    void testUpdateCourse() {
        Course courseToUpdate = new Course();
        courseToUpdate.setId(1);
        courseToUpdate.setTitle("Updated Course");
        courseToUpdate.setDescription("Updated Description");
        when(courseRepository.getCourseById(courseToUpdate.getId())).thenReturn(courseToUpdate);
        doNothing().when(courseRepository).updateCourse(courseToUpdate);
        courseService.updateCourse(courseToUpdate);
        verify(courseRepository, times(1)).updateCourse(courseToUpdate);
    }

    @Test
    void testDeleteCourse() {
        int courseId = 1;
        doNothing().when(courseRepository).deleteCourse(courseId);
        courseService.deleteCourse(courseId);
        verify(courseRepository, times(1)).deleteCourse(courseId);
    }

    @Test
    void testEnrollStudentInCourse() {
        int courseId = 1;
        int studentId = 1;
        doNothing().when(courseRepository).enrollStudentInCourse(courseId, studentId);
        courseService.enrollStudentInCourse(courseId, studentId);
        verify(courseRepository, times(1)).enrollStudentInCourse(courseId, studentId);
    }

    @Test
    void testRemoveStudentFromCourse() {
        int courseId = 1;
        int studentId = 1;
        doNothing().when(courseRepository).removeStudentFromCourse(courseId, studentId);
        courseService.removeStudentFromCourse(courseId, studentId);
        verify(courseRepository, times(1)).removeStudentFromCourse(courseId, studentId);
    }



}