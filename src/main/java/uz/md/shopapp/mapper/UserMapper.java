package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.user.UserDto;
import uz.md.shopapp.dtos.user.UserRegisterDto;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface UserMapper extends EntityMapper<User, UserDto> {

    @Override
    @Mapping(target = "permissions", source = "role.permissions")
    UserDto toDto(User entity);

    User fromAddDto(UserRegisterDto dto);

}
