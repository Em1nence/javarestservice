package service;

import model.Student;
import repository.impl.StudentRepository;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getStudentById(int id) {
        return studentRepository.getById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAll();
    }

    @Override
    public void addStudent(Student student) {
        studentRepository.add(student);
    }

    @Override
    public void deleteStudent(int id) {
        studentRepository.delete(id);
    }
}
