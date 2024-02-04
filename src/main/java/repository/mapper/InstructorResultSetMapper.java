package repository.mapper;

import model.Instructor;

import java.sql.ResultSet;

public interface InstructorResultSetMapper {
    Instructor mapResultSetToInstructor(ResultSet resultSet);
}
