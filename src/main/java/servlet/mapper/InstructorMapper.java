package servlet.mapper;

import model.Instructor;
import servlet.dto.InstructorDTO;

public class InstructorMapper implements DtoMapper<Instructor, InstructorDTO> {

    @Override
    public Instructor toEntity(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setId(dto.getId());
        instructor.setName(dto.getName());
        instructor.setEmail(dto.getEmail());
        return instructor;
    }

    @Override
    public InstructorDTO toDto(Instructor entity) {
        if (entity == null) {
            return null;
        }
        InstructorDTO dto = new InstructorDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}