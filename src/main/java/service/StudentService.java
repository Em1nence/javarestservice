package service;

import model.Instructor;
import model.Student;

import java.util.List;

public interface StudentService {
    Student getStudentById(int id);

    List<Student> getAllStudents();

    void addStudent(Student student);

    void deleteStudent(int id);
}
