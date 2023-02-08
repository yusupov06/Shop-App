package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.shopapp.domain.Address;
import uz.md.shopapp.dtos.address.AddressAddDto;
import uz.md.shopapp.dtos.address.AddressDto;
import uz.md.shopapp.dtos.address.AddressEditDto;

@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<Address, AddressDto> {

    @Override
    @Mapping(target = "userId" , source = "user.id")
    AddressDto toDto(Address entity);

    Address fromAddDto(AddressAddDto dto);

    Address fromEditDto(AddressEditDto editDto);
}
