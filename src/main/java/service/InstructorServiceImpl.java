package service;

import db.ConnectionManager;
import model.Instructor;
import repository.impl.InstructorRepository;

import java.util.List;

public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    public InstructorServiceImpl(){
        this.instructorRepository = new InstructorRepository(new ConnectionManager());
    }

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
    public void updateInstructor(Instructor instructor) { instructorRepository.updateInstructor(instructor); }
    @Override
    public void addInstructor(Instructor instructor) {
        instructorRepository.addInstructor(instructor);
    }

    @Override
    public void deleteInstructor(int id) {
        instructorRepository.deleteInstructor(id);
    }
}
