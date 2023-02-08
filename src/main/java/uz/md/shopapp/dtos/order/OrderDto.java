package uz.md.shopapp.dtos.order;


import lombok.*;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.dtos.address.AddressDto;
import uz.md.shopapp.dtos.orderProduct.OrderProductDto;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDto {
    private Long id;
    private UUID userId;
    private OrderStatus status;
    private Double overallPrice;
    private AddressDto address;
    private List<OrderProductDto> orderProducts;
}
