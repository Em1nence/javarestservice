package repository.mapper;

import model.Student;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentResultSetMapperImpl implements StudentResultSetMapper {

    @Override
    public Student mapResultSetToStudent(ResultSet resultSet) {
        Student student = new Student();
        try {
            student.setId(resultSet.getInt("id"));
            student.setName(resultSet.getString("name"));
            student.setEmail(resultSet.getString("email"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return student;
    }
}
