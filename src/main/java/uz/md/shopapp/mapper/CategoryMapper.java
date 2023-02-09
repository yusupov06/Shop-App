package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDto;
import uz.md.shopapp.dtos.category.CategoryEditDto;
import uz.md.shopapp.dtos.category.CategoryInfoDto;
import uz.md.shopapp.views.CategoryView;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CategoryMapper extends EntityMapper<Category, CategoryDto> {

    Category fromAddDto(CategoryAddDTO dto);

    Category fromEditDto(CategoryEditDto dto, @MappingTarget Category category);

    default List<CategoryInfoDto> toInfoDtoList(List<CategoryView> all) {

        List<CategoryInfoDto> infoDtoList = new ArrayList<>();
        for (CategoryView categoryView : all) {
            infoDtoList.add(toInfoDto(categoryView));
        }
        return infoDtoList;
    }

    default CategoryInfoDto toInfoDto(CategoryView category) {
        return new CategoryInfoDto(
                Long.parseLong(category.getId()),
                category.getName(),
                category.getDescription());
    }

    @Override
    @Mapping(target = "products", expression = " java( productMapper.toDtoList( entity.getProducts() ) ) ")
    CategoryDto toDto(Category entity);
}