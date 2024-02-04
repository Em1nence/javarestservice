package repository.mapper;

import model.Instructor;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstructorResultSetMapperImpl implements InstructorResultSetMapper {

    @Override
    public Instructor mapResultSetToInstructor(ResultSet resultSet) {
        Instructor instructor = new Instructor();
        try {
            instructor.setId(resultSet.getInt("id"));
            instructor.setName(resultSet.getString("name"));
            instructor.setEmail(resultSet.getString("email"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return instructor;
    }
}