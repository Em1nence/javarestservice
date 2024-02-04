package repository.mapper;

import model.Instructor;
import model.Student;

import java.sql.ResultSet;

public interface StudentResultSetMapper {
    Student mapResultSetToStudent(ResultSet resultSet);

}
