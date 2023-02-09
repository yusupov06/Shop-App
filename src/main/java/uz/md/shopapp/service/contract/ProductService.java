package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.product.ProductAddDto;
import uz.md.shopapp.dtos.product.ProductDto;
import uz.md.shopapp.dtos.product.ProductEditDto;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;

import java.util.List;

public interface ProductService {

    ApiResult<ProductDto> findById(Long id);

    ApiResult<ProductDto> add(ProductAddDto dto);

    ApiResult<ProductDto> edit(ProductEditDto editDto);

    ApiResult<Void> delete(Long id);

    ApiResult<List<ProductDto>> getAllByCategory(Long id);

    ApiResult<List<ProductDto>> findAllBySimpleSearch(SimpleSearchRequest request);

    ApiResult<List<ProductDto>> findAllBySort(SimpleSortRequest request);

    ApiResult<List<ProductDto>> findAllByPagination(String page);
}
