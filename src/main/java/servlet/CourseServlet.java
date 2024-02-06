package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import service.CourseServiceImpl;
import servlet.dto.CourseIncomingDTO;
import servlet.dto.CourseOutgoingDTO;
import servlet.mapper.CourseMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/course/*")
public class CourseServlet extends HttpServlet {
    private final CourseServiceImpl courseService;
    private final CourseMapper courseMapper; // Добавили поле для CourseMapper

    public CourseServlet(CourseServiceImpl courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Получение всех курсов
            List<CourseOutgoingDTO> courses = courseService.getAllCourses().stream()
                    .map(course -> courseMapper.toOutgoingDto(course)) // Используем courseMapper
                    .collect(Collectors.toList());

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(courses));
        } else {
            // Получение конкретного курса
            int courseId = Integer.parseInt(pathInfo.substring(1)); // Убираем первый символ '/'
            CourseOutgoingDTO course = courseMapper.toOutgoingDto(courseService.getCourseById(courseId)); // Используем courseMapper

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(course));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Создание нового курса
        CourseIncomingDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), CourseIncomingDTO.class);
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
            CourseIncomingDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), CourseIncomingDTO.class);
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
