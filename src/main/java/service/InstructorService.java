package service;

import model.Course;
import model.Instructor;

import java.util.List;

public interface InstructorService {
    Instructor getInstructorById(int id);

    List<Instructor> getAllInstructors();

    void updateInstructor(Instructor instructor);

    void addInstructor(Instructor instructor);

    void deleteInstructor(int id);
}
