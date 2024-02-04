package service;

import model.Instructor;

import java.util.List;

public interface InstructorService {
    Instructor getInstructorById(int id);

    List<Instructor> getAllInstructors();

    void addInstructor(Instructor instructor);

    void deleteInstructor(int id);
}
