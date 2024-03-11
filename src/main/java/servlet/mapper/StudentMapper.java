package servlet.mapper;

import model.Student;
import servlet.dto.StudentDTO;

public class StudentMapper implements DtoMapper<Student, StudentDTO> {

    @Override
    public Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }

    @Override
    public StudentDTO toDto(Student entity) {
        if (entity == null) {
            return null;
        }
        StudentDTO dto = new StudentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}
