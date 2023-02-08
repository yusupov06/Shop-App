package uz.md.shopapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.TokenDTO;
import uz.md.shopapp.dtos.user.UserLoginDto;
import uz.md.shopapp.dtos.user.UserRegisterDto;
import uz.md.shopapp.exceptions.ConflictException;
import uz.md.shopapp.exceptions.NotEnabledException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.AuthService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    private static final String DEFAULT_PHONE_NUMBER = "+998931668648";

    private static final String DEFAULT_FIRSTNAME = "Ali";

    private static final String DEFAULT_LASTNAME = "Yusupov";
    private static final String DEFAULT_PASSWORD = "123";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    private User user;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void init() {
        user = new User();
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setActive(true);
        user.setEnabled(true);
        user.setDeleted(false);
        user.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setRole(roleRepository.save(new Role("ADMIN","develop", Set.of(PermissionEnum.values()))));
    }

    @Test
    @Transactional
    void shouldAddUser() {

        userRepository.deleteAll();
        UserRegisterDto registerDto = new UserRegisterDto("user1", "user1", "+998931112233", "123");
        ApiResult<Void> result = authService.register(registerDto);

        assertTrue(result.isSuccess());
        List<User> all = userRepository.findAll();
        User added = all.get(0);

        assertEquals(added.getFirstName(), registerDto.getFirstName());
        assertEquals(added.getLastName(), registerDto.getLastName());
        assertEquals(added.getPhoneNumber(), registerDto.getPhoneNumber());
    }

    @Test
    @Transactional
    void shouldNotAddWithAlreadyExistedPhoneNumber() {
        userRepository.saveAndFlush(user);
        UserRegisterDto registerDto = new UserRegisterDto("user1", "user1", user.getPhoneNumber(), "123");
        assertThrows(ConflictException.class, () -> authService.register(registerDto));
    }

    @Test
    @Transactional
    void shouldLoginUser() {
        userRepository.saveAndFlush(user);

        UserLoginDto userLoginDto = new UserLoginDto(user.getPhoneNumber(), DEFAULT_PASSWORD);

        ApiResult<TokenDTO> login = authService.login(userLoginDto);
        assertTrue(login.isSuccess());
        TokenDTO data = login.getData();
        assertNotNull(data.getAccessToken());
        assertNotNull(data.getRefreshToken());
    }


    @Test
    @Transactional
    void shouldNotLoginWithNotFound() {
        userRepository.deleteAll();
        user.setDeleted(true);
        userRepository.saveAndFlush(user);
        UserLoginDto userLoginDto = new UserLoginDto(user.getPhoneNumber(), user.getPassword());
        assertThrows(NotFoundException.class, () -> authService.login(userLoginDto));
    }

    @Test
    @Transactional
    void shouldNotLoginDisableUser() {
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
        UserLoginDto userLoginDto = new UserLoginDto(DEFAULT_PHONE_NUMBER, user.getPassword());
        assertThrows(NotEnabledException.class, () -> authService.login(userLoginDto));
    }

    @Test
    @Transactional
    void shouldNotLoginWithWrongUsernameOrPassword() {
        userRepository.saveAndFlush(user);
        UserLoginDto userLoginDto = new UserLoginDto(DEFAULT_PHONE_NUMBER, "wrong");
        assertThrows(NotFoundException.class, () -> authService.login(userLoginDto));
    }


}
