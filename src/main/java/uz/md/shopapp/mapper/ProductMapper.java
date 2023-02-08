package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Product;
import uz.md.shopapp.dtos.product.ProductAddDto;
import uz.md.shopapp.dtos.product.ProductDto;
import uz.md.shopapp.dtos.product.ProductEditDto;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<Product, ProductDto> {

    Product fromAddDto(ProductAddDto dto);

    @Override
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product entity);

    Product fromEditDto(ProductEditDto editDto, @MappingTarget Product product);
}
