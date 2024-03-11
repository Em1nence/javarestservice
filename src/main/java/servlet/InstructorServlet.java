package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import db.ConnectionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.Instructor;
import repository.impl.InstructorRepository;
import service.CourseServiceImpl;
import service.InstructorService;
import service.InstructorServiceImpl;
import servlet.dto.CourseDTO;
import servlet.dto.InstructorDTO;
import servlet.mapper.CourseMapper;
import servlet.mapper.InstructorMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/instructor/*")
public class InstructorServlet extends HttpServlet {
    private final InstructorService instructorService;
    private final InstructorMapper instructorMapper;

    public InstructorServlet(InstructorMapper instructorMapper, InstructorServiceImpl instructorService) {
        this.instructorService = instructorService;
        this.instructorMapper = instructorMapper;
    }
    public InstructorServlet(InstructorMapper instructorMapper){
        this.instructorMapper = instructorMapper;
        this.instructorService = new InstructorServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Получение всех инструкторов
            List<InstructorDTO> instructors = instructorService.getAllInstructors().stream()
                    .map(instructor -> instructorMapper.toDto(instructor))
                    .collect(Collectors.toList());

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(instructors));
        } else {
            // Получение конкретного инструктора
            int instructorId = Integer.parseInt(pathInfo.substring(1)); // Убираем первый символ '/'
            InstructorDTO instructor = instructorMapper.toDto(instructorService.getInstructorById(instructorId));

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(instructor));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Создание нового инструктора
        InstructorDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), InstructorDTO.class);
        Instructor instructor = instructorMapper.toEntity(incomingDTO);
        instructorService.addInstructor(instructor);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int instructorId = Integer.parseInt(request.getPathInfo().substring(1));
        Instructor existingInstructor = instructorService.getInstructorById(instructorId);

        if (existingInstructor != null) {
            InstructorDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), InstructorDTO.class);
            Instructor updatedInstructor = instructorMapper.toEntity(incomingDTO);
            updatedInstructor.setId(instructorId);
            instructorService.updateInstructor(updatedInstructor);

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Удаление инструктора
        int instructorId = Integer.parseInt(request.getPathInfo().substring(1)); // Убираем первый символ '/'
        Instructor existingInstructor = instructorService.getInstructorById(instructorId);

        if (existingInstructor != null) {
            instructorService.deleteInstructor(instructorId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
