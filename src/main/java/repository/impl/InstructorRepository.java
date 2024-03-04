package repository.impl;

import db.ConnectionManager;
import model.Instructor;
import repository.mapper.InstructorResultSetMapper;
import repository.mapper.InstructorResultSetMapperImpl;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorRepository {
    private final ConnectionManager connectionManager;
    private final InstructorResultSetMapper irsm;
    public InstructorRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.irsm = new InstructorResultSetMapperImpl();
    }

    private Connection getConnection() {
        try {
            return connectionManager.getConnection();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to get a database connection", e);
        }
    }

    public List<Instructor> getAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        try(Connection connection = getConnection()) {
            String sql = "SELECT * FROM Instructor";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Instructor instructor = irsm.mapResultSetToInstructor(resultSet);
                    instructors.add(instructor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructors;
    }

    public Instructor getInstructorById(int id) {
        Instructor instructor = null;

        try(Connection connection = getConnection()) {
            String sql = "SELECT * FROM Instructor WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        instructor = irsm.mapResultSetToInstructor(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructor;
    }

    public void addInstructor(Instructor instructor) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO Instructor (name, email) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, instructor.getName());
                statement.setString(2, instructor.getEmail());

                statement.executeUpdate();

                // Получение сгенерированного ключа (если необходимо)
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        instructor.setId(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding an instructor", e);
        }
    }

    public void updateInstructor(Instructor instructor) {

        try(Connection connection = getConnection()){
            String sql = "UPDATE Instructor SET name = ?, email = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, instructor.getName());
                statement.setString(2, instructor.getEmail());
                statement.setInt(3, instructor.getId());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInstructor(int id) {
        try(Connection connection = getConnection()) {
            String sql = "DELETE FROM Instructor WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
