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
        return instructorRepository.getById(id);
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.getAll();
    }

    @Override
    public void addInstructor(Instructor instructor) {
        instructorRepository.add(instructor);
    }

    @Override
    public void deleteInstructor(int id) {
        instructorRepository.delete(id);
    }
}
