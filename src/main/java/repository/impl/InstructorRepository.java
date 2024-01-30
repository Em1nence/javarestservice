/*
package repository.impl;

import model.Instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstructorRepository {

    private final Connection connection;

    public InstructorRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Instructor> getAll() {
        List<Instructor> instructors = new ArrayList<>();
        String query = "SELECT * FROM Course";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                instructors.add(new Instructor(id, name, email));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработайте исключение согласно вашим требованиям
        }
        return instructors;
    }

    public Instructor getById(int id) {
        String query = "SELECT * FROM instructor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    return new Instructor(id, name, email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(Instructor instructor) {
        String query = "INSERT INTO instructors (name, email) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, instructor.getName());
            preparedStatement.setString(2, instructor.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}*/
