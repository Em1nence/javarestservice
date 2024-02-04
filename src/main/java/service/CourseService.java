package service;

import model.Course;

import java.util.List;

public interface CourseService {
    Course getCourseById(int id);

    List<Course> getAllCourses();

    void addCourse(Course course);

    void updateCourse(Course course);

    void deleteCourse(int id);

    void enrollStudentInCourse(int courseId, int studentId);

    void removeStudentFromCourse(int courseId, int studentId);
}