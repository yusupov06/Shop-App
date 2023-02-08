package uz.md.shopapp.dtos.orderProduct;

import lombok.*;
import uz.md.shopapp.dtos.product.ProductDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderProductDto {
    private Long id;
    private Long orderId;
    private ProductDto product;
    private int quantity;
    private double price;
}
