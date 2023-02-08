package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.user.UserDto;

public interface UserService {
    ApiResult<UserDto> findById(Long id);

    ApiResult<Void> delete(Long id);
}
