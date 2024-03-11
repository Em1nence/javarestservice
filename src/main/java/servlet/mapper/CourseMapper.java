package servlet.mapper;

import model.Course;
import model.Instructor;
import model.Student;
import servlet.dto.CourseDTO;
import servlet.dto.StudentDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper implements DtoMapper<Course, CourseDTO> {

    private final StudentMapper studentMapper;
    private final InstructorMapper instructorMapper;

    public CourseMapper(StudentMapper studentMapper, InstructorMapper instructorMapper) {
        this.studentMapper = studentMapper;
        this.instructorMapper = instructorMapper;
    }

    @Override
    public Course toEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }
        Course entity = new Course();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        if (dto.getInstructor() != null) {
            entity.setInstructor(instructorMapper.toEntity(dto.getInstructor()));
        }

        // Пример маппинга коллекции students
        List<Student> students = dto.getStudents().stream()
                .map(studentMapper::toEntity)
                .collect(Collectors.toList());
        entity.setStudents(students);

        return entity;
    }

    @Override
    public CourseDTO toDto(Course entity) {
        if (entity == null) {
            return null; // или выбрасывайте исключение или устанавливайте значения по умолчанию
        }
        CourseDTO dto = new CourseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        if (entity.getInstructor() != null) {
            dto.setInstructor(instructorMapper.toDto(entity.getInstructor()));
        }

        // Пример маппинга коллекции students
        List<StudentDTO> studentDTOs = entity.getStudents().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
        dto.setStudents(studentDTOs);

        return dto;
    }
}