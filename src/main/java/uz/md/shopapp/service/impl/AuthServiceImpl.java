package uz.md.shopapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
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
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.exceptions.NotEnabledException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.UserMapper;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.AuthService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           @Lazy AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
            throw new NotEnabledException("USER_IS_DISABLED");
        } catch (BadCredentialsException | UsernameNotFoundException badCredentialsException) {
            throw new NotFoundException("USER_NOT_FOUND_OR_USERNAME_NOT_FOUND");
        }

        LocalDateTime tokenIssuedAt = LocalDateTime.now();
        String accessToken = jwtTokenProvider.generateAccessToken(user, Timestamp.valueOf(tokenIssuedAt));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);

        return ApiResult.successResponse(
                tokenDTO);
    }

    @Override
    public ApiResult<TokenDTO> refreshToken(String accessToken, String refreshToken) {

        accessToken = accessToken.substring(accessToken.indexOf("Bearer") + 6).trim();

        if (!jwtTokenProvider.isValidToken(accessToken, true)) {
            try {
                String userId = jwtTokenProvider.extractUserId(accessToken, true);
                User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() ->
                        new NotFoundException("USER_ID_NOT_FOUND"));

                if (!user.isEnabled()
                        || !user.isAccountNonExpired()
                        || !user.isAccountNonLocked()
                        || !user.isCredentialsNonExpired())
                    throw new NotAllowedException("USER_PERMISSION_RESTRICTION");

                LocalDateTime tokenIssuedAt = LocalDateTime.now();
                String newAccessToken = jwtTokenProvider.generateAccessToken(user, Timestamp.valueOf(tokenIssuedAt));
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

                TokenDTO tokenDTO = TokenDTO.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                return ApiResult.successResponse(tokenDTO);
            } catch (Exception e) {
                throw new NotAllowedException("REFRESH_TOKEN_EXPIRED");
            }

        }

        throw new NotAllowedException("ACCESS_TOKEN_NOT_EXPIRED");
    }

    @Override
    public ApiResult<Void> register(UserRegisterDto dto) {
        log.info("User registration with " + dto);

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber()))
            throw new ConflictException("PHONE_NUMBER_ALREADY_EXISTS");

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
        return userRepository.findByPhoneNumber(username).orElseThrow(() ->
                new UsernameNotFoundException("USER_NOT_FOUND_WITH_USERNAME"));
    }
}
