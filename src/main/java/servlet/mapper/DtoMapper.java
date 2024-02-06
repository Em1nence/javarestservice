package servlet.mapper;

public interface DtoMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
