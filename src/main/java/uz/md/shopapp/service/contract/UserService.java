package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.user.UserDto;

import java.util.UUID;

public interface UserService {
    ApiResult<UserDto> findById(UUID id);

    ApiResult<Void> delete(UUID id);
}
