package service;

import model.Course;
import model.Instructor;
import repository.impl.CourseRepository;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course getCourseById(int id) {
        return courseRepository.getById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.getAll();
    }

    @Override
    public void addCourse(Course course) {
        courseRepository.add(course);
    }

    @Override
    public void updateCourse(Course course) {
        courseRepository.update(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.delete(id);
    }

    @Override
    public void enrollStudentInCourse(int courseId, int studentId) {
        courseRepository.enrollStudentInCourse(courseId, studentId);
    }

    @Override
    public void removeStudentFromCourse(int courseId, int studentId) {
        courseRepository.removeStudentFromCourse(courseId, studentId);
    }
}