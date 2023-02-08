package uz.md.shopapp.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryAddDto;
import uz.md.shopapp.dtos.category.CategoryDto;
import uz.md.shopapp.dtos.category.CategoryEditDto;
import uz.md.shopapp.dtos.category.CategoryInfoDto;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.CategoryMapper;
import uz.md.shopapp.repository.CategoryRepository;
import uz.md.shopapp.service.contract.CategoryService;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MessageSource messageSource;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper, MessageSource messageSource) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.messageSource = messageSource;
    }

    @Override
    public ApiResult<CategoryDto> add(CategoryAddDto dto) {

        if (categoryRepository.existsByName(dto.getName()))
            throw new AlreadyExistsException(messageSource
                    .getMessage("CATEGORY_NAME_ALREADY_EXISTS", null,
                            LocaleContextHolder.getLocale()));

        return ApiResult
                .successResponse(categoryMapper
                        .toDto(categoryRepository
                                .save(categoryMapper
                                        .fromAddDto(dto))));
    }

    @Override
    public ApiResult<CategoryDto> findById(Long id) {
        return ApiResult.successResponse(categoryMapper
                .toDto(categoryRepository
                        .findById(id)
                        .orElseThrow(() -> {
                            throw new NotFoundException(messageSource
                                    .getMessage("CATEGORY_NOT_FOUND", null, LocaleContextHolder.getLocale()));
                        })));
    }

    @Override
    public ApiResult<CategoryDto> edit(CategoryEditDto editDto) {

        Category editing = categoryRepository
                .findById(editDto.getId())
                .orElseThrow(() -> {
                    throw new NotFoundException(messageSource
                            .getMessage("CATEGORY_NOT_FOUND", null, LocaleContextHolder.getLocale()));
                });

        if (categoryRepository.existsByNameAndIdIsNot(editDto.getName(), editing.getId()))
            throw new AlreadyExistsException(messageSource
                    .getMessage("CATEGORY_NAME_ALREADY_EXISTS", null, LocaleContextHolder.getLocale()));

        Category category = categoryMapper.fromEditDto(editDto, editing);

        return ApiResult.successResponse(categoryMapper
                .toDto(categoryRepository.save(category)));
    }

    @Override
    public ApiResult<List<CategoryDto>> getAll() {
        return ApiResult.successResponse(
                categoryMapper.toDtoList(
                        categoryRepository.findAll()
                )
        );
    }

    @Override
    public ApiResult<List<CategoryInfoDto>> getAllForInfo() {
        return ApiResult.successResponse(
                categoryMapper.toInfoDtoList(
                        categoryRepository.findAllForInfo()
                ));
    }

    @Override
    public ApiResult<Void> delete(Long id) {

        if (!categoryRepository.existsById(id))
            throw new NotFoundException(messageSource
                    .getMessage("CATEGORY_NOT_FOUND", null, LocaleContextHolder.getLocale()));

        categoryRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
