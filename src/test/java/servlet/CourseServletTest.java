/*
package servlet;

import model.Instructor;
import model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import service.CourseServiceImpl;
import servlet.dto.CourseDTO;
import servlet.dto.StudentDTO;
import servlet.mapper.CourseMapper;
import servlet.mapper.InstructorMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseServletTest {
    @Mock
    private CourseServiceImpl courseService;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private CourseServlet courseServlet;
    @Mock
    private InstructorMapper instructorMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseServlet = new CourseServlet(courseMapper);
    }

    @Test
    void testDoGetAllCourses() throws IOException {
        // Создаем инструкторов
        Instructor instructor1 = new Instructor(1, "John Doe", "john@example.com");
        Instructor instructor2 = new Instructor(2, "Jane Smith", "jane@example.com");

        // Создаем студентов (если они нужны)
        List<StudentDTO> studentDtos = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        // Добавляем студентов в список, если это необходимо

        // Создаем курсы
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1, "Math", "Mathematics course", instructor1, students));
        courses.add(new Course(2, "Physics", "Physics course", instructor2, students));

        // Моки для сервиса курсов
        when(courseService.getAllCourses()).thenReturn(courses);

        // Моки для маппера курсов
        when(courseMapper.toDto(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            return new CourseDTO(course.getId(), course.getTitle(), course.getDescription(), instructorMapper.toDto(course.getInstructor()), studentDtos);
        });

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new java.io.PrintWriter(stringWriter));

        courseServlet.doGet(request, response);

        // Ожидаемый ответ
        String expectedResponse = "[{\"id\":1,\"title\":\"Math\",\"description\":\"Mathematics course\",\"instructorId\":1,\"students\":[]}" +
                ",{\"id\":2,\"title\":\"Physics\",\"description\":\"Physics course\",\"instructorId\":2,\"students\":[]}]";
        assertEquals(expectedResponse, stringWriter.toString().trim());
    }


    @Test
    void testDoGetSpecificCourse() throws ServletException, IOException {

        Instructor instructor1 = new Instructor(1, "John Doe", "john@example.com");
        Instructor instructor2 = new Instructor(2, "Jane Smith", "jane@example.com");
        // Создаем курс и его DTO
        Course course = new Course(1, "Math", "Mathematics course", instructor1, new ArrayList<>());
        CourseDTO courseDTO = new CourseDTO(1, "Math", "Mathematics course", instructorMapper.toDto(instructor1), new ArrayList<>());

        // Устанавливаем путь запроса
        when(request.getPathInfo()).thenReturn("/1");

        // Моки для сервиса курсов
        when(courseService.getCourseById(1)).thenReturn(course);

        // Моки для маппера курсов
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new java.io.PrintWriter(stringWriter));

        // Вызываем метод doGet
        courseServlet.doGet(request, response);

        // Проверяем, что установлены правильные заголовки ответа
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        // Ожидаемый ответ
        String expectedResponse = "{\"id\":1,\"title\":\"Math\",\"description\":\"Mathematics course\",\"instructorId\":1,\"students\":[]}";
        // Проверяем, что ответ сервера соответствует ожидаемому ответу
        verify(response.getWriter()).write(expectedResponse);
    }

    @Test
    void testDoPost() throws IOException {
        String json = "{\"id\":1,\"name\":\"Math\"}";
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(reader);

        CourseDTO courseDTO = new CourseDTO(1, "Math");
        Course course = new Course(1, "Math");

        when(courseMapper.toEntity(courseDTO)).thenReturn(course);

        courseServlet.doPost(request, response);

        verify(courseService).addCourse(course);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPutExistingCourse() throws IOException {
        String json = "{\"id\":1,\"name\":\"Math\"}";
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(reader);

        CourseDTO courseDTO = new CourseDTO(1, "Math");
        Course course = new Course(1, "Math");

        when(courseService.getCourseById(1)).thenReturn(course);
        when(courseMapper.toEntity(courseDTO)).thenReturn(course);

        courseServlet.doPut(request, response);

        verify(courseService).updateCourse(course);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutNonExistingCourse() throws ServletException, IOException {
        String json = "{\"id\":1,\"name\":\"Math\"}";
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(reader);

        when(courseService.getCourseById(1)).thenReturn(null);

        courseServlet.doPut(request, response);

        verify(courseService, never()).updateCourse(any());
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoDeleteExistingCourse() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        Course course = new Course(1, "Math");
        when(courseService.getCourseById(1)).thenReturn(course);

        courseServlet.doDelete(request, response);

        verify(courseService).deleteCourse(1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDeleteNonExistingCourse() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(courseService.getCourseById(1)).thenReturn(null);

        courseServlet.doDelete(request, response);

        verify(courseService, never()).deleteCourse(anyInt());
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}*/
