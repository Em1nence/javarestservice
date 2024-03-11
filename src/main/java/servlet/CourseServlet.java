package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import service.CourseService;
import service.CourseServiceImpl;
import servlet.dto.CourseDTO;
import servlet.mapper.CourseMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/course/*")
public class CourseServlet extends HttpServlet {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    public CourseServlet(CourseMapper courseMapper, CourseServiceImpl courseService) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }
    public CourseServlet(CourseMapper courseMapper){
        this.courseMapper = courseMapper;
        this.courseService = new CourseServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Получение всех курсов
            List<CourseDTO> courses = courseService.getAllCourses().stream()
                    .map(courseMapper::toDto) // Используем courseMapper
                    .collect(Collectors.toList());

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(courses));
        } else {
            // Получение конкретного курса
            int courseId = Integer.parseInt(pathInfo.substring(1)); // Убираем первый символ '/'
            CourseDTO course = courseMapper.toDto(courseService.getCourseById(courseId)); // Используем courseMapper

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(course));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Создание нового курса
        CourseDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), CourseDTO.class);
        Course course = courseMapper.toEntity(incomingDTO); // Используем courseMapper
        courseService.addCourse(course);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Обновление существующего курса
        int courseId = Integer.parseInt(request.getPathInfo().substring(1)); // Убираем первый символ '/'
        Course existingCourse = courseService.getCourseById(courseId);

        if (existingCourse != null) {
            CourseDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), CourseDTO.class);
            Course updatedCourse = courseMapper.toEntity(incomingDTO); // Используем courseMapper
            updatedCourse.setId(courseId);
            courseService.updateCourse(updatedCourse);

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        // Удаление курса
        int courseId = Integer.parseInt(request.getPathInfo().substring(1)); // Убираем первый символ '/'
        Course existingCourse = courseService.getCourseById(courseId);

        if (existingCourse != null) {
            courseService.deleteCourse(courseId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
