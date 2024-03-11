package servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.CourseService;
import service.CourseServiceImpl;
import servlet.dto.CourseDTO;
import servlet.mapper.CourseMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourseServletTest2 {

    private CourseServlet courseServlet;
    private CourseService courseService;
    private CourseMapper courseMapper;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() {
        courseService = Mockito.mock(CourseServiceImpl.class);
        courseMapper = Mockito.mock(CourseMapper.class);
        courseServlet = new CourseServlet(courseMapper, (CourseServiceImpl) courseService);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    public void testDoGet_AllCourses() throws Exception {
        // Arrange
        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        courseServlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response.getWriter()).write(anyString());
        verify(courseService).getAllCourses();
        verify(courseMapper, atLeastOnce()).toDto(any());
    }

    @Test
    public void testDoGet_SingleCourse() throws Exception {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);
        when(response.getWriter()).thenReturn(printWriter);
        when(courseService.getCourseById(courseId)).thenReturn(new Course());
        when(courseMapper.toDto(any())).thenReturn(new CourseDTO());

        // Act
        courseServlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response.getWriter()).write(anyString());
        verify(courseService).getCourseById(courseId);
        verify(courseMapper, atLeastOnce()).toDto(any());
    }

    @Test
    public void testDoPost() throws Exception {
        // Arrange
        ServletInputStream inputStreamMock = Mockito.mock(ServletInputStream.class);
        when(request.getInputStream()).thenReturn(inputStreamMock);
        when(courseMapper.toEntity(any())).thenReturn(new Course());

        // Act
        courseServlet.doPost(request, response);

        // Assert
        verify(courseMapper).toEntity(any());
        verify(courseService).addCourse(any());
        assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void testDoPut() throws Exception {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);
        ServletInputStream inputStreamMock = Mockito.mock(ServletInputStream.class);
        when(request.getInputStream()).thenReturn(inputStreamMock);
        when(courseService.getCourseById(courseId)).thenReturn(new Course());
        when(courseMapper.toEntity(any())).thenReturn(new Course());

        // Act
        courseServlet.doPut(request, response);

        // Assert
        verify(courseService).getCourseById(courseId);
        verify(courseMapper).toEntity(any());
        verify(courseService).updateCourse(any());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void testDoDelete() throws Exception {
        // Arrange
        int courseId = 1;
        when(request.getPathInfo()).thenReturn("/" + courseId);
        when(courseService.getCourseById(courseId)).thenReturn(new Course());

        // Act
        courseServlet.doDelete(request, response);

        // Assert
        verify(courseService).getCourseById(courseId);
        verify(courseService).deleteCourse(courseId);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}