package uz.md.shopapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.md.shopapp.config.security.JwtTokenProvider;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.TokenDTO;
import uz.md.shopapp.dtos.user.UserLoginDto;
import uz.md.shopapp.dtos.user.UserRegisterDto;
import uz.md.shopapp.exceptions.ConflictException;
import uz.md.shopapp.exceptions.NotEnabledException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.UserMapper;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.AuthService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           MessageSource messageSource,
                           @Lazy AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public ApiResult<TokenDTO> login(UserLoginDto dto) {

        log.info("User login method called: " + dto);
        User user;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getPhoneNumber(),
                            dto.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            user = (User) authentication.getPrincipal();
        } catch (DisabledException | LockedException | CredentialsExpiredException disabledException) {
            throw new NotEnabledException(messageSource.getMessage("USER_IS_DISABLED", null, LocaleContextHolder.getLocale()));
        } catch (BadCredentialsException | UsernameNotFoundException badCredentialsException) {
            throw new NotFoundException(messageSource.getMessage("USER_NOT_FOUND_OR_USERNAME_NOT_FOUND", null, LocaleContextHolder.getLocale()));
        }

        LocalDateTime tokenIssuedAt = LocalDateTime.now();
        String accessToken = jwtTokenProvider.generateAccessToken(user, Timestamp.valueOf(tokenIssuedAt));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);

        return ApiResult.successResponse(
                tokenDTO);
    }

    @Override
    public ApiResult<Void> register(UserRegisterDto dto) {
        log.info("User registration with " + dto);

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber()))
            throw new ConflictException(messageSource.getMessage("PHONE_NUMBER_ALREADY_EXISTS", null, LocaleContextHolder.getLocale()));

        User user = userMapper.fromAddDto(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);
        userRepository.save(user);
        return ApiResult.successResponse();
    }


    @Override
    public ApiResult<Void> activate(String activationCode) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(username).orElseThrow(() -> new UsernameNotFoundException("User Not found with username " + username));
    }
}
