package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDto;
import uz.md.shopapp.dtos.category.CategoryEditDto;
import uz.md.shopapp.dtos.category.CategoryInfoDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<Category, CategoryDto> {

    Category fromAddDto(CategoryAddDTO dto);

    Category fromEditDto(CategoryEditDto dto, @MappingTarget Category category);

    List<CategoryInfoDto> toInfoDtoList(List<Category> all);

    CategoryInfoDto toInfoDto(Category category);
}