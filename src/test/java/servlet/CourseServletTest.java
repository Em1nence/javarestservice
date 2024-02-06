package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import service.CourseServiceImpl;
import servlet.dto.CourseIncomingDTO;
import servlet.dto.CourseOutgoingDTO;
import servlet.mapper.CourseMapper;

import java.io.*;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServletTest {

    @Mock
    private CourseServiceImpl courseService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private CourseServlet courseServlet;

    private CourseMapper courseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseServlet = new CourseServlet(courseService);
        courseMapper = new CourseMapper();
    }

    @Test
    void testDoGetWithAllCourses() throws ServletException, IOException {
        // Arrange
        doReturn(Arrays.asList(
                new Course(1, "Course1", "Description1", new Instructor(1, "Instructor1")),
                new Course(2, "Course2", "Description2", new Instructor(2, "Instructor2"))
        )).when(courseService).getAllCourses();

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Act
        courseServlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedJson = "[{\"id\":1,\"title\":\"Course1\",\"description\":\"Description1\",\"instructorId\":1}," +
                "{\"id\":2,\"title\":\"Course2\",\"description\":\"Description2\",\"instructorId\":2}]";

        writer.flush();
        verify(response.getWriter()).write(expectedJson);
    }

    @Test
    void testDoGetWithSpecificCourse() throws ServletException, IOException {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);
        when(courseService.getCourseById(courseId)).thenReturn(
                new Course(courseId, "Course1", "Description1", new Instructor(1, "Instructor1"))
        );

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Act
        courseServlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedJson = "{\"id\":1,\"title\":\"Course1\",\"description\":\"Description1\",\"instructorId\":1}";

        writer.flush();
        verify(response.getWriter()).write(expectedJson);
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        // Arrange
        CourseIncomingDTO incomingDTO = new CourseIncomingDTO();
        incomingDTO.setTitle("NewCourse");
        incomingDTO.setDescription("NewDescription");
        incomingDTO.setInstructorId(1);

        when(new ObjectMapper().readValue(request.getReader(), CourseIncomingDTO.class)).thenReturn(incomingDTO);

        // Act
        courseServlet.doPost(request, response);

        // Assert
        verify(courseService).addCourse(courseMapper.toEntity(incomingDTO));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPutWithValidCourseId() throws ServletException, IOException {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);

        Course existingCourse = new Course(courseId, "ExistingCourse", "ExistingDescription", new Instructor(1, "Instructor1"));
        when(courseService.getCourseById(courseId)).thenReturn(existingCourse);

        CourseIncomingDTO incomingDTO = new CourseIncomingDTO();
        incomingDTO.setTitle("UpdatedCourse");
        incomingDTO.setDescription("UpdatedDescription");
        incomingDTO.setInstructorId(2);

        when(new ObjectMapper().readValue(request.getReader(), CourseIncomingDTO.class)).thenReturn(incomingDTO);

        // Act
        courseServlet.doPut(request, response);

        // Assert
        verify(courseService).updateCourse(courseMapper.toEntity(incomingDTO));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutWithInvalidCourseId() throws ServletException, IOException {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);
        when(courseService.getCourseById(courseId)).thenReturn(null);

        // Act
        courseServlet.doPut(request, response);

        // Assert
        verify(courseService, never()).updateCourse(any());
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoDeleteWithValidCourseId() {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);

        Course existingCourse = new Course(courseId, "ExistingCourse", "ExistingDescription", new Instructor(1, "Instructor1"));
        when(courseService.getCourseById(courseId)).thenReturn(existingCourse);

        // Act
        courseServlet.doDelete(request, response);

        // Assert
        verify(courseService).deleteCourse(courseId);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDeleteWithInvalidCourseId() {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);
        when(courseService.getCourseById(courseId)).thenReturn(null);

        // Act
        courseServlet.doDelete(request, response);

        // Assert
        verify(courseService, never()).deleteCourse(anyInt());
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}