package service;

import db.ConnectionManager;
import model.Course;
import repository.impl.CourseRepository;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(){
        this.courseRepository = new CourseRepository(new ConnectionManager());
    }
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course getCourseById(int id) {
        return courseRepository.getCourseById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses();
    }

    @Override
    public void addCourse(Course course) {
        courseRepository.addCourse(course);
    }

    @Override
    public void updateCourse(Course course) {
        courseRepository.updateCourse(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteCourse(id);
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