package uz.md.shopapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.user.UserDto;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.UserMapper;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ApiResult<UserDto> findById(UUID id) {
        return ApiResult.successResponse(userMapper.toDto(userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"))));
    }

    @Override
    public ApiResult<Void> delete(UUID id) {
        userRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
