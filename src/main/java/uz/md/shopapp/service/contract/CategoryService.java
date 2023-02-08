package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryAddDto;
import uz.md.shopapp.dtos.category.CategoryDto;
import uz.md.shopapp.dtos.category.CategoryEditDto;
import uz.md.shopapp.dtos.category.CategoryInfoDto;

import java.util.List;

public interface CategoryService {

    ApiResult<CategoryDto> add(CategoryAddDto dto);

    ApiResult<CategoryDto> findById(Long id);

    ApiResult<CategoryDto> edit(CategoryEditDto editDto);

    ApiResult<Void> delete(Long id);

    ApiResult<List<CategoryDto>> getAll();

    ApiResult<List<CategoryInfoDto>> getAllForInfo();
}
