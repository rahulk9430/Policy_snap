package com.insurance.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericMapper {

    private final ModelMapper modelMapper;

    public GenericMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // ✅ Entity to DTO
    public <D, T> D mapToDto(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    // ✅ DTO to Entity
    public <T, D> T mapToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    // ✅ List<Entity> to List<DTO>
    public <D, T> List<D> mapToDtoList(List<T> entities, Class<D> dtoClass) {
        return entities.stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    // ✅ List<DTO> to List<Entity>
    public <T, D> List<T> mapToEntityList(List<D> dtoList, Class<T> entityClass) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, entityClass))
                .collect(Collectors.toList());
    }
}
