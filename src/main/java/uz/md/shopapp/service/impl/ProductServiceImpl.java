package uz.md.shopapp.service.impl;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Product;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.product.ProductAddDto;
import uz.md.shopapp.dtos.product.ProductDto;
import uz.md.shopapp.dtos.product.ProductEditDto;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.ProductMapper;
import uz.md.shopapp.repository.CategoryRepository;
import uz.md.shopapp.repository.ProductRepository;
import uz.md.shopapp.service.QueryService;
import uz.md.shopapp.service.contract.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final QueryService queryService;
    private final MessageSource messageSource;

    private Product getById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("PRODUCT_NOT_FOUND_WITH_ID");
                });
    }

    @Override
    public ApiResult<ProductDto> findById(Long id) {
        Product byId = getById(id);
        return ApiResult.successResponse(
                productMapper.toDto(byId));
    }

    @Override
    public ApiResult<ProductDto> add(ProductAddDto dto) {

        if (productRepository.existsByName(dto.getName()))
            throw new AlreadyExistsException("PRODUCT_NAME_ALREADY_EXISTS");

        Product product = productMapper.fromAddDto(dto);

        Category category = categoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND"));

        product.setCategory(category);
        return ApiResult
                .successResponse(productMapper
                        .toDto(productRepository
                                .save(product)));
    }

    @Override
    public ApiResult<ProductDto> edit(ProductEditDto editDto) {

        Product product = productRepository
                .findById(editDto.getId())
                .orElseThrow(() -> {
                    throw new NotFoundException("PRODUCT_NOT_FOUND");
                });

        if (productRepository.existsByNameAndIdIsNot(editDto.getName(), product.getId()))
            throw new AlreadyExistsException("PRODUCT_NAME_ALREADY_EXISTS");

        Product edited = productMapper.fromEditDto(editDto, product);

        edited.setCategory(categoryRepository
                .findById(editDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND")));

        return ApiResult
                .successResponse(productMapper
                        .toDto(productRepository.save(edited)));
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        if (!productRepository.existsById(id))
            throw new NotFoundException("PRODUCT_DOES_NOT_EXIST");
        productRepository.deleteById(id);
        return ApiResult.successResponse();
    }

    @Override
    public ApiResult<List<ProductDto>> getAllByCategory(Long id) {
        if (!categoryRepository.existsById(id))
            throw new NotFoundException("CATEGORY_NOT_FOUND_WITH_ID");
        return ApiResult.successResponse(
                productMapper
                        .toDtoList(productRepository
                                .findAllByCategory_Id(id)));
    }

    @Override
    public ApiResult<List<ProductDto>> findAllBySimpleSearch(SimpleSearchRequest request) {

        TypedQuery<Product> productTypedQuery = queryService
                .generateSimpleSearchQuery(Product.class, request);

        return ApiResult
                .successResponse(productMapper
                        .toDtoList(productTypedQuery.getResultList()));
    }

    @Override
    public ApiResult<List<ProductDto>> findAllBySort(SimpleSortRequest request) {
        TypedQuery<Product> productTypedQuery = queryService
                .generateSimpleSortQuery(Product.class, request);

        return ApiResult
                .successResponse(productMapper
                        .toDtoList(productTypedQuery.getResultList()));
    }
}
