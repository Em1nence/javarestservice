package repository.impl;

import db.ConnectionManager;
import model.Course;
import model.Instructor;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    private final ConnectionManager connectionManager;


    public CourseRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    private Connection getConnection(){
        try {
            return connectionManager.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Course getById(int id) {
        Course course = null;
        Connection connection = getConnection();
        try {
            String sql = "SELECT c.*, i.* FROM Course c LEFT JOIN Instructor i ON c.instructor_id = i.id WHERE c.id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        course = mapResultSetToCourse(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return course;
    }

    public List<Course> getAll() {
        List<Course> courses = new ArrayList<>();
        Connection connection = getConnection();
        try {
            String sql = "SELECT c.*, i.* FROM Course c LEFT JOIN Instructor i ON c.instructor_id = i.id";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Course course = mapResultSetToCourse(resultSet);
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }
    public void add(Course course) {
        Connection connection = getConnection();
        try {
            String sql = "INSERT INTO Course (title, description, instructor_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, course.getTitle());
                statement.setString(2, course.getDescription());

                // В инструкторе есть поле id, которое будет привязано к instructor_id в таблице Course
                statement.setInt(3, course.getInstructor().getId());

                statement.executeUpdate();

                // Получение сгенерированного ключа (если необходимо)
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        course.setId(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course) {
        Connection connection = getConnection();
        try {
            String sql = "UPDATE Course SET title = ?, description = ?, instructor_id = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, course.getTitle());
                statement.setString(2, course.getDescription());
                statement.setInt(3, course.getInstructor().getId());
                statement.setInt(4, course.getId());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        Connection connection = getConnection();
        try {
            String sql = "DELETE FROM Course WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Course mapResultSetToCourse(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt("c.id"));
        course.setTitle(resultSet.getString("c.title"));
        course.setDescription(resultSet.getString("c.description"));

        Instructor instructor = new Instructor();
        instructor.setId(resultSet.getInt("i.id"));
        instructor.setName(resultSet.getString("i.name"));

        course.setInstructor(instructor);

        return course;
    }

}
