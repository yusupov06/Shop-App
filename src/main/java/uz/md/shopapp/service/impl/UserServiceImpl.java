package uz.md.shopapp.service.impl;

import org.springframework.stereotype.Service;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.user.UserDto;
import uz.md.shopapp.service.contract.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public ApiResult<UserDto> findById(Long id) {
        return null;
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        return null;
    }
}
