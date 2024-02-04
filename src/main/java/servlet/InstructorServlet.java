package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Instructor;
import service.InstructorService;
import service.InstructorServiceImpl;
import servlet.dto.InstructorIncomingDTO;
import servlet.dto.InstructorOutgoingDTO;
import servlet.mapper.InstructorMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/instructor/*")
public class InstructorServlet extends HttpServlet {
    private final InstructorServiceImpl instructorService;

    public InstructorServlet(InstructorServiceImpl instructorService) {
        this.instructorService = instructorService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Получение всех инструкторов
            List<InstructorOutgoingDTO> instructors = instructorService.getAllInstructors().stream()
                    .map(instructor -> new InstructorMapper().toOutgoingDto(instructor))
                    .collect(Collectors.toList());

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(instructors));
        } else {
            // Получение конкретного инструктора
            int instructorId = Integer.parseInt(pathInfo.substring(1)); // Убираем первый символ '/'
            InstructorOutgoingDTO instructor = new InstructorMapper().toOutgoingDto(instructorService.getInstructorById(instructorId));

            // Отправка данных в формате JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(instructor));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Создание нового инструктора
        InstructorIncomingDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), InstructorIncomingDTO.class);
        Instructor instructor = new InstructorMapper().toEntity(incomingDTO);
        instructorService.addInstructor(instructor);

        response.setStatus(HttpServletResponse.SC_CREATED);
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
