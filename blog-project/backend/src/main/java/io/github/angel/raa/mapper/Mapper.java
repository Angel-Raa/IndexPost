package io.github.angel.raa.mapper;

import java.util.List;

public interface Mapper<T, D> {
    T toEntity(D dto);

    D toDto(T entity);

    default List<T> toEntity(List<D> dtoList) {
        return dtoList.stream().map(this::toEntity).toList();
    }

    default List<D> toDto(List<T> entityList) {
        return entityList.stream().map(this::toDto).toList();
    }
}
