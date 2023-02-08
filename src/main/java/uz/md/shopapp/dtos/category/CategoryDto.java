package uz.md.shopapp.dtos.category;

import lombok.*;
import uz.md.shopapp.dtos.product.ProductDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private List<ProductDto> products;

    public CategoryDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
