package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.product.ProductAddDto;
import uz.md.shopapp.dtos.product.ProductDto;
import uz.md.shopapp.dtos.product.ProductEditDto;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.service.contract.ProductService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(ProductController.BASE_URL + "/")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    public static final String BASE_URL = AppConstants.BASE_URL + "product";

    private final ProductService productService;

    /**
     * Get list of products by category
     *
     * @param id category's id
     * @return list of products
     */
    @GetMapping("/category/{id}")
    public ApiResult<List<ProductDto>> getAllByCategory(@PathVariable Long id) {
        log.info("getAllByCategory called with category id {}", id);
        return productService.getAllByCategory(id);
    }

    /**
     * Get a product by id
     *
     * @param id product's id
     * @return found product
     */
    @GetMapping("/{id}")
    public ApiResult<ProductDto> getById(@PathVariable Long id) {
        log.info("getById called with id {}", id);
        return productService.findById(id);
    }

    /**
     * Adds a product
     *
     * @param dto product add dto
     * @return added product
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(description = "Add a product")
    public ApiResult<ProductDto> add(@RequestBody @Valid ProductAddDto dto) {
        log.info("Product Add");
        log.info("Request body {}", dto);
        return productService.add(dto);
    }

    /**
     * edits the product
     *
     * @param editDto product edit dto
     * @return edited product
     */
    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(description = "edit product")
    public ApiResult<ProductDto> edit(@RequestBody @Valid ProductEditDto editDto) {
        log.info("edit product");
        log.info("Request body {}", editDto);
        return productService.edit(editDto);
    }

    /**
     * deletes the product
     *
     * @param id product id
     * @return deleted product
     */
    @DeleteMapping("/delete/{id}")
    @ApiResponse(description = "Delete a product")
    public ApiResult<Void> delete(@PathVariable Long id) {
        log.info("delete product with id {}", id);
        return productService.delete(id);
    }

    /**
     * Simple Search for products
     *
     * @param request simple search request
     * @return List of products
     */
    @PostMapping("/search")
    @ApiResponse(description = "Searching products")
    public ApiResult<List<ProductDto>> getProductsBySimpleSearch(@RequestBody @Valid SimpleSearchRequest request) {
        log.info("get products by simple search request");
        log.info("Request body {}", request);
        return productService.findAllBySimpleSearch(request);
    }

    /**
     * Simple Sorting for products
     *
     * @param request sorting products dto
     * @return List of products sorted
     */
    @PostMapping("/sorting")
    @ApiResponse(description = "List of products sorted")
    public ApiResult<List<ProductDto>> getProductsBySort(@RequestBody SimpleSortRequest request) {
        log.info("getProductsBySort");
        log.info("Request body is: {}", request);
        return productService.findAllBySort(request);
    }

    /**
     * Simple Sorting for products
     *
     * @param page - pagination
     * @return List of products sorted
     */
    @GetMapping("/page/{page}")
    @ApiResponse(description = "List of products sorted")
    public ApiResult<List<ProductDto>> getProductsByPagination(@PathVariable String page) {
        log.info("getProductsBySort");
        log.info("Request body is: {}", page);
        return productService.findAllByPagination(page);
    }


}
