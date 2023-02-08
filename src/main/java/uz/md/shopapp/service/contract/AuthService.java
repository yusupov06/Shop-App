package uz.md.shopapp.service.contract;

import org.springframework.security.core.userdetails.UserDetailsService;
import uz.md.shopapp.dtos.*;
import uz.md.shopapp.dtos.user.UserLoginDto;
import uz.md.shopapp.dtos.user.UserRegisterDto;

public interface AuthService extends UserDetailsService {

    ApiResult<Void> register(UserRegisterDto dto);

    ApiResult<TokenDTO> login(UserLoginDto dto);

    ApiResult<Void> activate(String activationCode);

}
