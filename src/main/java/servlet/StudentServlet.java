package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Student;
import service.StudentService;
import service.StudentServiceImpl;
import servlet.dto.StudentIncomingDTO;
import servlet.mapper.StudentMapper;

import java.io.IOException;

@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentServlet(StudentServiceImpl studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Создание нового студента
        StudentIncomingDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), StudentIncomingDTO.class);
        Student student = studentMapper.toEntity(incomingDTO);
        studentService.addStudent(student);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)  {
        // Удаление студента
        int studentId = Integer.parseInt(request.getPathInfo().substring(1));
        Student existingStudent = studentService.getStudentById(studentId);

        if (existingStudent != null) {
            studentService.deleteStudent(studentId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}