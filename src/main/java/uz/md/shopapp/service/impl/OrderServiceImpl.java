package uz.md.shopapp.service.impl;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Address;
import uz.md.shopapp.domain.Order;
import uz.md.shopapp.domain.OrderProduct;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.order.OrderAddDto;
import uz.md.shopapp.dtos.order.OrderDto;
import uz.md.shopapp.dtos.order.OrderProductAddDto;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.exceptions.IllegalRequestException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.AddressMapper;
import uz.md.shopapp.mapper.OrderMapper;
import uz.md.shopapp.mapper.OrderProductMapper;
import uz.md.shopapp.repository.*;
import uz.md.shopapp.service.QueryService;
import uz.md.shopapp.service.contract.OrderService;
import uz.md.shopapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;
    private final QueryService queryService;
    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    /**
     * getting by id
     *
     * @param id order's id
     * @return the order
     */
    private Order getById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException(messageSource.getMessage("ORDER_NOT_FOUND_WITH_ID", null, LocaleContextHolder.getLocale()) + id);
                });
    }


    @Override
    public ApiResult<OrderDto> findById(Long id) {
        return ApiResult.successResponse(
                orderMapper.toDto(getById(id)));
    }


    @Override
    public ApiResult<OrderDto> add(OrderAddDto dto) {

        Order order = new Order();
        UUID userId = dto.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(messageSource
                        .getMessage("USER_NOT_FOUND", null,
                                LocaleContextHolder.getLocale())));

        Address address;
        if (dto.getAddressId() != null) {
            address = addressRepository
                    .findByIdAndUserId(dto.getAddressId(), user.getId())
                    .orElseThrow(() -> new NotFoundException(messageSource
                            .getMessage("ADDRESS_NOT_FOUND", null,
                                    LocaleContextHolder.getLocale())));
        } else if (dto.getAddress() != null) {
            Address adding = addressMapper.fromAddDto(dto.getAddress());
            adding.setUser(user);
            address = addressRepository.save(adding);
        } else {
            throw new IllegalRequestException(messageSource
                    .getMessage("ADDRESS_MUST_BE_GIVEN_FOR_ORDER", null, LocaleContextHolder.getLocale()));
        }

        order.setUser(user);
        order.setAddress(address);
        order.setOverallPrice(dto.getOverallPrice());
        order.setActive(true);
        order.setDeleted(false);
        orderRepository.save(order);
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductAddDto addDto : dto.getOrderProducts()) {
            OrderProduct orderProduct = orderProductMapper.fromAddDto(addDto);
            orderProduct.setOrder(order);
            orderProduct.setProduct(productRepository
                    .findById(addDto.getProductId())
                    .orElseThrow(() -> new NotFoundException(messageSource
                            .getMessage("ORDER_PRODUCT_NOT_FOUND", null,
                                    LocaleContextHolder.getLocale()))));
            orderProductRepository.save(orderProduct);
            orderProducts.add(orderProduct);
        }

        order.setOrderProducts(orderProducts);

        return ApiResult
                .successResponse(orderMapper
                        .toDto(order));
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        if (!orderRepository.existsById(id))
            throw new NotFoundException(messageSource
                    .getMessage("ORDER_DOES_NOT_EXIST", null,
                            LocaleContextHolder.getLocale()));
        orderRepository.deleteById(id);
        return ApiResult.successResponse();
    }


    @Override
    public ApiResult<List<OrderDto>> getAllByPage(String pagination) {
        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult.successResponse(
                orderMapper.toDtoList(orderRepository
                        .findAll(PageRequest.of(page[0], page[1])).getContent()));
    }


    @Override
    public ApiResult<List<OrderDto>> findAllBySort(SimpleSortRequest request) {
        TypedQuery<Order> typedQuery = queryService.generateSimpleSortQuery(Order.class, request);
        return ApiResult
                .successResponse(orderMapper
                        .toDtoList(typedQuery.getResultList()));
    }


    @Override
    public ApiResult<List<OrderDto>> getOrdersByStatus(String status, String pagination) {
        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult
                .successResponse(orderMapper
                        .toDtoList(orderRepository
                                .findAllByStatus(OrderStatus.valueOf(status),
                                        PageRequest.of(page[0], page[1])).getContent()));
    }

    @Override
    public ApiResult<List<OrderDto>> getOrdersByUserId(UUID userid, String pagination) {
        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult
                .successResponse(orderMapper
                        .toDtoList(orderRepository
                                .findAllByUserId(userid,
                                        PageRequest.of(page[0], page[1])).getContent()));
    }
}
