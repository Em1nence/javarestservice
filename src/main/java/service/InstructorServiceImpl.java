package service;

import model.Instructor;
import repository.impl.InstructorRepository;

import java.util.List;

public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public Instructor getInstructorById(int id) {
        return instructorRepository.getInstructorById(id);
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.getAllInstructors();
    }

    @Override
    public void addInstructor(Instructor instructor) {
        instructorRepository.addInstructor(instructor);
    }

    @Override
    public void deleteInstructor(int id) {
        instructorRepository.deleteInstructor(id);
    }
}
