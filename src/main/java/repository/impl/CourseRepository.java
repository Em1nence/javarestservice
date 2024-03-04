package repository.impl;

import db.ConnectionManager;
import model.Course;
import model.Instructor;
import model.Student;
import repository.mapper.CourseResultSetMapper;
import repository.mapper.CourseResultSetMapperImpl;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    private final ConnectionManager connectionManager;
    private final CourseResultSetMapper crsm;

    public CourseRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.crsm = new CourseResultSetMapperImpl();
    }
    private Connection getConnection() {
        try {
            return connectionManager.getConnection();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to get a database connection", e);
        }
    }

    public Course getCourseById(int id) {
        Course course = null;
        try(Connection connection = getConnection()) {
            String sql = "SELECT c.*, i.* FROM Course c LEFT JOIN Instructor i ON c.instructor_id = i.id WHERE c.id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        course = crsm.mapResultSetToCourse(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course by ID", e);
        }

        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT c.*, i.* FROM Course c LEFT JOIN Instructor i ON c.instructor_id = i.id";

            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Course course = crsm.mapResultSetToCourse(resultSet);
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all courses", e);
        }

        return courses;
    }
    public void addCourse(Course course) {

        if (!instructorExists(course.getInstructor().getId())) {
            throw new IllegalArgumentException("Instructor with the specified ID does not exist.");
        }

        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO Course (title, description, instructor_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, course.getTitle());
                statement.setString(2, course.getDescription());
                statement.setInt(3, course.getInstructor().getId());

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        course.setId(generatedId);
                    }
                }
            }
            Instructor instructor = course.getInstructor();
            instructor.getCourses().add(course);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding a course", e);
        }
    }


    public void updateCourse(Course course) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE Course SET title = ?, description = ?, instructor_id = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, course.getTitle());
                statement.setString(2, course.getDescription());
                statement.setInt(3, course.getInstructor().getId());
                statement.setInt(4, course.getId());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating a course", e);
        }
    }

    public void deleteCourse(int id) {
        try (Connection connection = getConnection()) {
            String deleteStudentCourseSql = "DELETE FROM studentcourse WHERE course_id = ?";
            try (PreparedStatement deleteStudentCourseStatement = connection.prepareStatement(deleteStudentCourseSql)) {
                deleteStudentCourseStatement.setInt(1, id);
                deleteStudentCourseStatement.executeUpdate();
            }
            String sql = "DELETE FROM Course WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                statement.executeUpdate();
            }
            Course deletedCourse = getCourseById(id);
            if (deletedCourse != null) {
                Instructor instructor = deletedCourse.getInstructor();
                instructor.getCourses().remove(deletedCourse);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting a course", e);
        }
    }


    public void enrollStudentInCourse(int courseId, int studentId) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO studentcourse (course_id, student_id) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, courseId);
                statement.setInt(2, studentId);
                statement.executeUpdate();

                // Обновление коллекции внутри объекта Course

                Course course = getCourseById(courseId);
                StudentRepository studentRepository = new StudentRepository(connectionManager);
                Student student = studentRepository.getStudentById(studentId);

                if (course != null && student != null) {
                    course.getStudents().add(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error enrolling student in course", e);
        }
    }

    public void removeStudentFromCourse(int courseId, int studentId) {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM studentcourse WHERE course_id = ? AND student_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, courseId);
                statement.setInt(2, studentId);
                statement.executeUpdate();

                // Обновление коллекции внутри объекта Course
                Course course = getCourseById(courseId);
                StudentRepository studentRepository = new StudentRepository(connectionManager);
                Student student = studentRepository.getStudentById(studentId);

                if (course != null && student != null) {
                    course.getStudents().remove(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error removing student from course", e);
        }
    }
    private boolean instructorExists(int instructorId) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT id FROM Instructor WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, instructorId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking instructor existence", e);
        }
    }



}
