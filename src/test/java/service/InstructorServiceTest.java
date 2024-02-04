package service;

import model.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.impl.InstructorRepository;



import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class InstructorServiceTest {
    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorServiceImpl instructorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInstructors() {
        when(instructorRepository.getAll()).thenReturn(new ArrayList<>());
        List<Instructor> instructors = instructorService.getAllInstructors();
        assertNotNull(instructors);
        verify(instructorRepository, times(1)).getAll();
    }

    @Test
    void testGetInstructorById() {
        int instructorId = 1;
        Instructor expectedInstructor = new Instructor();
        expectedInstructor.setId(instructorId);
        when(instructorRepository.getById(instructorId)).thenReturn(expectedInstructor);
        Instructor actualInstructor = instructorService.getInstructorById(instructorId);
        assertNotNull(actualInstructor);
        assertEquals(expectedInstructor, actualInstructor);
        verify(instructorRepository, times(1)).getById(instructorId);
    }

    @Test
    void testAddInstructor() {
        Instructor newInstructor = new Instructor();
        newInstructor.setName("New Instructor");
        newInstructor.setEmail("new.instructor@example.com");
        doNothing().when(instructorRepository).add(newInstructor);
        instructorService.addInstructor(newInstructor);
        verify(instructorRepository, times(1)).add(newInstructor);
    }

    @Test
    void testDeleteInstructor() {
        int instructorId = 1;
        doNothing().when(instructorRepository).delete(instructorId);
        instructorService.deleteInstructor(instructorId);
        verify(instructorRepository, times(1)).delete(instructorId);
    }

}
