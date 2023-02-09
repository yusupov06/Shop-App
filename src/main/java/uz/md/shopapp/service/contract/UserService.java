package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.user.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ApiResult<UserDto> findById(UUID id);

    ApiResult<Void> delete(UUID id);

    ApiResult<UserDto> me();

    ApiResult<List<UserDto>> findAll();

}
