package uz.md.shopapp.mini_client;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.domain.Client;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryInfoDto;
import uz.md.shopapp.dtos.client.ClientAddDto;
import uz.md.shopapp.dtos.client.ClientDto;
import uz.md.shopapp.dtos.product.ProductDto;
import uz.md.shopapp.service.contract.ClientService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(ClientController.BASE_URL)
@RequiredArgsConstructor
public class ClientController {

    public static final String BASE_URL = AppConstants.BASE_URL + "client";
    private static final String GET_ALL_CATEGORIES = "/get_all_categories";
    private static final String GET_PRODUCTS_BY_CATEGORY = "/get_products";

    private final ClientService clientService;

    @PostMapping
    public ApiResult<ClientDto> getKey(@RequestBody @Valid ClientAddDto addDto){
        return clientService.getKey(addDto);
    }

    @GetMapping(GET_ALL_CATEGORIES)
    public ApiResult<List<CategoryInfoDto>> getAllCategories(@RequestParam String access_key) {
        return clientService.getAllCategories(access_key);
    }

    @GetMapping(GET_PRODUCTS_BY_CATEGORY)
    public ApiResult<List<ProductDto>> getAllProductsByCategory(@RequestParam String access_key, @RequestParam String category_name) {
        return clientService.getAllProductsByCategory(access_key, category_name);
    }

}
