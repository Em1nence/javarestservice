package servlet.mapper;

import model.Course;
import model.Instructor;
import model.Student;
import servlet.dto.CourseIncomingDTO;
import servlet.dto.CourseOutgoingDTO;
import servlet.dto.StudentOutgoingDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper implements DtoMapper<Course, CourseIncomingDTO, CourseOutgoingDTO> {

    private final StudentMapper studentMapper;

    public CourseMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public Course toEntity(CourseIncomingDTO incomingDTO) {
        Course course = new Course();
        course.setId(incomingDTO.getId());
        course.setTitle(incomingDTO.getTitle());
        course.setDescription(incomingDTO.getDescription());

        // Установка инструктора (если instructorId не равен 0)
        if (incomingDTO.getInstructorId() != 0) {
            Instructor instructor = new Instructor();
            instructor.setId(incomingDTO.getInstructorId());
            course.setInstructor(instructor);
        }
        return course;
    }

    @Override
    public CourseOutgoingDTO toOutgoingDto(Course entity) {
        if (entity == null) {
            return null; // или выбрасывайте исключение или устанавливайте значения по умолчанию
        }
        CourseOutgoingDTO outgoingDTO = new CourseOutgoingDTO();
        outgoingDTO.setId(entity.getId());
        outgoingDTO.setTitle(entity.getTitle());
        outgoingDTO.setDescription(entity.getDescription());
        if (entity.getInstructor() != null) {
            outgoingDTO.setInstructorId(entity.getInstructor().getId());
        }

        // Пример маппинга коллекции students
        List<StudentOutgoingDTO> studentDTOs = entity.getStudents().stream()
                .map(studentMapper::toOutgoingDto)
                .collect(Collectors.toList());
        outgoingDTO.setStudents(studentDTOs);

        return outgoingDTO;
    }
}