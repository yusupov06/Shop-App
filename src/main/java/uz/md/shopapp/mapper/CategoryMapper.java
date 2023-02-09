package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDto;
import uz.md.shopapp.dtos.category.CategoryEditDto;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CategoryMapper extends EntityMapper<Category, CategoryDto> {

    Category fromAddDto(CategoryAddDTO dto);

    Category fromEditDto(CategoryEditDto dto, @MappingTarget Category category);

    @Override
    @Mapping(target = "products", expression = " java( productMapper.toDtoList( entity.getProducts() ) ) ")
    CategoryDto toDto(Category entity);
}