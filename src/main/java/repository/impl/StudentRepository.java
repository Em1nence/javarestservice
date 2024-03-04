package repository.impl;

import db.ConnectionManager;
import model.Student;
import repository.mapper.StudentResultSetMapper;
import repository.mapper.StudentResultSetMapperImpl;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private final ConnectionManager connectionManager;
    private final StudentResultSetMapper srsm;

    public StudentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.srsm = new StudentResultSetMapperImpl();
    }

    private Connection getConnection() {
        try {
            return connectionManager.getConnection();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to get a database connection", e);
        }
    }

    public void addStudent(Student student) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO Student (name, email) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, student.getName());
                statement.setString(2, student.getEmail());

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        student.setId(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding a student", e);
        }
    }

    // Получение студента по ID
    public Student getStudentById(int id) {
        Student student = null;
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM Student WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        student = srsm.mapResultSetToStudent(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching student by ID", e);
        }
        return student;
    }

    // Получение всех студентов
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM Student";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Student student = srsm.mapResultSetToStudent(resultSet);
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all students", e);
        }
        return students;
    }

    public void deleteStudent(int id) {
        try (Connection connection = getConnection()) {
            String deleteStudentCourseSql = "DELETE FROM studentcourse WHERE student_id = ?";
            try (PreparedStatement deleteStudentCourseStatement = connection.prepareStatement(deleteStudentCourseSql)) {
                deleteStudentCourseStatement.setInt(1, id);
                deleteStudentCourseStatement.executeUpdate();
            }
            String sql = "DELETE FROM student WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting a student", e);
        }
    }

    public List<Student> getStudentsByCourseId(int courseId) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT s.* FROM Student s " +
                    "JOIN studentcourse cs ON s.id = cs.student_id " +
                    "WHERE cs.course_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, courseId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Student student = new Student();
                        student.setId(resultSet.getInt("id"));
                        student.setName(resultSet.getString("name"));
                        student.setEmail(resultSet.getString("email"));
                        students.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching students by course ID", e);
        }
        return students;
    }



}
