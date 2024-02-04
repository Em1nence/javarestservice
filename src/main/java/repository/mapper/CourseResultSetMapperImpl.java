package repository.mapper;

import model.Course;
import model.Instructor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseResultSetMapperImpl implements CourseResultSetMapper {

    public Course mapResultSetToCourse(ResultSet resultSet) {
        Course course = new Course();
        try {
            course.setId(resultSet.getInt("c.id"));

            course.setTitle(resultSet.getString("c.title"));
            course.setDescription(resultSet.getString("c.description"));

            Instructor instructor = new Instructor();
            instructor.setId(resultSet.getInt("i.id"));
            instructor.setName(resultSet.getString("i.name"));

            course.setInstructor(instructor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return course;

    }
}