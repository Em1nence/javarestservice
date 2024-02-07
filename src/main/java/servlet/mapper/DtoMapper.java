package servlet.mapper;

public interface DtoMapper<E, I, O> {
    E toEntity(I incomingDto);
    O toOutgoingDto(E entity);
}
