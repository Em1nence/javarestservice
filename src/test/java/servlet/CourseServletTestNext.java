/*
package servlet;

import db.ConnectionManager;
import model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.impl.CourseRepository;
import service.CourseService;
import service.CourseServiceImpl;
import servlet.dto.CourseDTO;
import servlet.mapper.CourseMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class CourseServletTestNext {

    private CourseServlet courseServlet;
    private CourseMapper courseMapper;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() {
        CourseRepository courseRepository = new CourseRepository(new ConnectionManager());
        CourseService courseService = new CourseServiceImpl(courseRepository);
        courseMapper = Mockito.mock(CourseMapper.class);
        courseServlet = new CourseServlet(courseMapper);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    public void testGetAllCourses() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(new Course(), new Course()));
        when(courseMapper.toDto(any())).thenReturn(new CourseDTO());
        when(response.getWriter()).thenReturn(printWriter);

        courseServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response.getWriter()).write(anyString());
    }

    @Test
    public void testGetCourseById() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(courseService.getCourseById(1)).thenReturn(new Course());
        when(courseMapper.toDto(any())).thenReturn(new CourseDTO());
        when(response.getWriter()).thenReturn(printWriter);

        courseServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response.getWriter()).write(anyString());
    }

    @Test
    public void testAddCourse() throws ServletException, IOException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
        courseServlet.doPost(request, response);

        verify(courseService).addCourse(any());
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testUpdateCourse() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
        when(courseService.getCourseById(1)).thenReturn(new Course());
        courseServlet.doPut(request, response);

        verify(courseService).updateCourse(any());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDeleteCourse() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(courseService.getCourseById(1)).thenReturn(new Course());
        courseServlet.doDelete(request, response);

        verify(courseService).deleteCourse(1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}*/
