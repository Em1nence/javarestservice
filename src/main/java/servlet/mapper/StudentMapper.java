package servlet.mapper;

import model.Student;
import servlet.dto.StudentIncomingDTO;
import servlet.dto.StudentOutgoingDTO;

public class StudentMapper implements DtoMapper<Student, StudentIncomingDTO, StudentOutgoingDTO> {

    @Override
    public Student toEntity(StudentIncomingDTO incomingDTO) {
        Student student = new Student();
        student.setId(incomingDTO.getId());
        student.setName(incomingDTO.getName());
        student.setEmail(incomingDTO.getEmail());
        return student;
    }

    @Override
    public StudentOutgoingDTO toOutgoingDto(Student entity) {
        StudentOutgoingDTO outgoingDTO = new StudentOutgoingDTO();
        outgoingDTO.setId(entity.getId());
        outgoingDTO.setName(entity.getName());
        outgoingDTO.setEmail(entity.getEmail());
        return outgoingDTO;
    }
}
