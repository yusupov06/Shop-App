package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import uz.md.shopapp.domain.OrderProduct;
import uz.md.shopapp.dtos.order.OrderProductAddDto;
import uz.md.shopapp.dtos.orderProduct.OrderProductDto;

@Mapper(componentModel = "spring")
public interface OrderProductMapper extends EntityMapper<OrderProduct, OrderProductDto> {

    OrderProduct fromAddDto(OrderProductAddDto addDto);
}
