package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.shopapp.domain.Order;
import uz.md.shopapp.dtos.order.OrderAddDto;
import uz.md.shopapp.dtos.order.OrderDto;
import uz.md.shopapp.service.contract.UserService;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class,
                OrderMapper.class,
                OrderProductMapper.class})
public interface OrderMapper extends EntityMapper<Order, OrderDto> {

    Order fromAddDto(OrderAddDto dto);

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "address", expression = " java( addressMapper.toDto(entity.getAddress()) ) ")
    @Mapping(target = "orderProducts", expression = " java( orderProductMapper.toDtoList(entity.getOrderProducts()) )")
    OrderDto toDto(Order entity);
}
