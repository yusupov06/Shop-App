package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryInfoDto;
import uz.md.shopapp.dtos.client.ClientAddDto;
import uz.md.shopapp.dtos.client.ClientDto;
import uz.md.shopapp.dtos.product.ProductDto;

import java.util.List;

public interface ClientService {
    ApiResult<List<CategoryInfoDto>> getAllCategories(String accessKey);

    ApiResult<List<ProductDto>> getAllProductsByCategory(String accessKey, String categoryName);

    ApiResult<ClientDto> getKey(ClientAddDto addDto);
}
