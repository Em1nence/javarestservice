package servlet.mapper;

public interface DtoMapper<E, I> {
    E toEntity(I incomingDto);
    I toDto(E entity);
}
