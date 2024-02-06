package servlet.mapper;

import model.Instructor;
import servlet.dto.InstructorIncomingDTO;
import servlet.dto.InstructorOutgoingDTO;

public class InstructorMapper implements DtoMapper<Instructor, InstructorIncomingDTO, InstructorOutgoingDTO> {

    @Override
    public Instructor toEntity(InstructorIncomingDTO incomingDTO) {
        Instructor instructor = new Instructor();
        instructor.setId(incomingDTO.getId());
        instructor.setName(incomingDTO.getName());
        instructor.setEmail(incomingDTO.getEmail());
        return instructor;
    }

    @Override
    public InstructorOutgoingDTO toOutgoingDto(Instructor entity) {
        InstructorOutgoingDTO outgoingDTO = new InstructorOutgoingDTO();
        outgoingDTO.setId(entity.getId());
        outgoingDTO.setName(entity.getName());
        outgoingDTO.setEmail(entity.getEmail());
        return outgoingDTO;
    }
}