package service;

import model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.impl.StudentRepository;
import service.StudentServiceImpl;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.getAll()).thenReturn(new ArrayList<>());
        List<Student> students = studentService.getAllStudents();
        assertNotNull(students);
        verify(studentRepository, times(1)).getAll();
    }

    @Test
    void testGetStudentById() {
        int studentId = 1;
        Student expectedStudent = new Student();
        expectedStudent.setId(studentId);
        when(studentRepository.getById(studentId)).thenReturn(expectedStudent);
        Student actualStudent = studentService.getStudentById(studentId);
        assertNotNull(actualStudent);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository, times(1)).getById(studentId);
    }

    @Test
    void testAddStudent() {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setEmail("new.student@example.com");
        doNothing().when(studentRepository).add(newStudent);
        studentService.addStudent(newStudent);
        verify(studentRepository, times(1)).add(newStudent);
    }

    @Test
    void testDeleteStudent() {
        int studentId = 1;
        doNothing().when(studentRepository).delete(studentId);
        studentService.deleteStudent(studentId);
        verify(studentRepository, times(1)).delete(studentId);
    }



}