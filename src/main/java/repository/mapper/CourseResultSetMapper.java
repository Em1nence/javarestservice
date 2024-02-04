package repository.mapper;

import model.Course;

import java.sql.ResultSet;

public interface CourseResultSetMapper {
    Course mapResultSetToCourse(ResultSet resultSet);
}
