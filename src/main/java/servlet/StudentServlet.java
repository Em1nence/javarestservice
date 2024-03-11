package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import db.ConnectionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Instructor;
import model.Student;
import repository.impl.StudentRepository;
import service.InstructorServiceImpl;
import service.StudentService;
import service.StudentServiceImpl;
import servlet.dto.InstructorDTO;
import servlet.dto.StudentDTO;
import servlet.mapper.InstructorMapper;
import servlet.mapper.StudentMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentServlet(StudentMapper studentMapper, StudentServiceImpl studentService) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }
    public StudentServlet(StudentMapper studentMapper){
        this.studentService = new StudentServiceImpl();
        this.studentMapper = studentMapper;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<StudentDTO> students = studentService.getAllStudents().stream()
                    .map(student -> studentMapper.toDto(student))
                    .collect(Collectors.toList());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(students));
        } else {
            int studentId = Integer.parseInt(pathInfo.substring(1)); // Убираем первый символ '/'
            StudentDTO student = studentMapper.toDto(studentService.getStudentById(studentId));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(student));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Создание нового студента
        StudentDTO incomingDTO = new ObjectMapper().readValue(request.getReader(), StudentDTO.class);
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