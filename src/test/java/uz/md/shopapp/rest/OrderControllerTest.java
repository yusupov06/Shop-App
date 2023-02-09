package uz.md.shopapp.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uz.md.shopapp.IntegrationTest;
import uz.md.shopapp.controller.AuthController;
import uz.md.shopapp.controller.OrderController;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressAddDto;
import uz.md.shopapp.dtos.address.AddressDto;
import uz.md.shopapp.dtos.order.OrderAddDto;
import uz.md.shopapp.dtos.order.OrderDto;
import uz.md.shopapp.dtos.order.OrderProductAddDto;
import uz.md.shopapp.dtos.orderProduct.OrderProductDto;
import uz.md.shopapp.dtos.product.ProductDto;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.dtos.user.UserLoginDto;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.OrderService;
import uz.md.shopapp.util.TestUtil;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.md.shopapp.controller.AuthController.LOGIN_URL;
import static uz.md.shopapp.controller.OrderController.BASE_URL;

/**
 * Integration tests for {@link OrderController}
 */
@IntegrationTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {


    @Value("${app.admin.firstName}")
    private String firstName;

    @Value("${app.admin.phoneNumber}")
    private String phoneNumber;

    @Value("${app.admin.password}")
    private String password;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderService orderService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static boolean setUpIsDone = false;
    private String accessToken;

    @BeforeEach
    void init() throws Exception {
        if (!setUpIsDone) {
            addAdmin();
            saveUserRole();
            setUpIsDone = true;
        }
    }

    private void saveUserRole() {
        roleRepository.save(
                new Role("USER",
                        "System USER",
                        Set.of(PermissionEnum.GET_PRODUCT)
                )
        );
    }

    private void addAdmin() {
        userRepository.save(new User(
                firstName,
                "",
                phoneNumber,
                passwordEncoder.encode(password),
                addAdminRole(),
                true
        ));
    }

    private Role addAdminRole() {
        return roleRepository.save(
                new Role("ADMIN",
                        "System owner",
                        Set.of(PermissionEnum.values())
                )
        );
    }

    @SuppressWarnings("unchecked")
    private String obtainAccessToken(String phoneNumber, String password) throws Exception {

        UserLoginDto userLoginDto = new UserLoginDto(phoneNumber, password);

        ResultActions result
                = mvc.perform(MockMvcRequestBuilders
                        .post(AuthController.BASE_URL + LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userLoginDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, String> data = (LinkedHashMap<String, String>) jsonParser.parseMap(resultString).get("data");
        System.out.println("data = " + data);
        return data.get("accessToken");
    }

    @Test
    void shouldAdd() throws Exception {

        accessToken = obtainAccessToken(phoneNumber,password);

        UUID userId = UUID.randomUUID();
        OrderAddDto addDto = getAddDto(userId);
        OrderDto orderDto = getOrderDto(userId);

        ApiResult<OrderDto> result = ApiResult.successResponse(orderDto);

        when(orderService.add(ArgumentMatchers.any())).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/add")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(addDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
        ;
    }

    private OrderDto getOrderDto(UUID userId) {

        return new OrderDto(1L,
                userId,
                OrderStatus.PREPARING,
                500.0,
                new AddressDto(1L, 15, "street", "city", userId),
                List.of(
                        new OrderProductDto(1L, 1L, new ProductDto(), 2, 50.0),
                        new OrderProductDto(1L, 1L, new ProductDto(), 2, 50.0)
                )
        );
    }

    private OrderAddDto getAddDto(UUID userId) {

        return new OrderAddDto(
                userId,
                new AddressAddDto(15, "street", "city", userId),
                null,
                500.0,
                List.of(
                        new OrderProductAddDto(1L, 2, 50.0),
                        new OrderProductAddDto(2L, 2, 50.0)
                ));
    }

    @Test
    void shouldGetById() throws Exception {

        accessToken = obtainAccessToken(phoneNumber,password);

        UUID userId = UUID.randomUUID();
        OrderDto orderDto = getOrderDto(userId);
        ApiResult<OrderDto> result = ApiResult.successResponse(orderDto);
        when(orderService.findById(1L)).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/by_id/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
        ;
    }


    @Test
    void shouldDelete() throws Exception {

        accessToken = obtainAccessToken(phoneNumber,password);


        ApiResult<Void> result = ApiResult.successResponse();
        when(orderService.delete(ArgumentMatchers.any())).thenReturn(result);
        mvc.perform(MockMvcRequestBuilders
                        .delete(BASE_URL + "/delete/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllByPage() throws Exception {

        accessToken = obtainAccessToken(phoneNumber,password);


        ApiResult<List<OrderDto>> result = ApiResult.successResponse();
        when(orderService.getAllByPage("0-5")).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/by_page/0-5")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void shouldGetAllByStatusAndPagination() throws Exception {

        accessToken = obtainAccessToken(phoneNumber,password);


        ApiResult<List<OrderDto>> result = ApiResult.successResponse();
        when(orderService.getOrdersByStatus("PREPARING", "0-5")).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/status/PREPARING/0-5")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void shouldGetAllByUserId() throws Exception {

        accessToken = obtainAccessToken(phoneNumber,password);

        UUID userid = UUID.randomUUID();
        ApiResult<List<OrderDto>> result = ApiResult.successResponse();
        when(orderService.getOrdersByUserId(userid, "0-5")).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/user/" + userid + "/0-5")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void shouldGetAllBySort() throws Exception {

        accessToken = obtainAccessToken(phoneNumber,password);

        SimpleSortRequest sortRequest = new SimpleSortRequest("userId", Sort.Direction.ASC, 0, 15);

        ApiResult<List<OrderDto>> result = ApiResult.successResponse(new ArrayList<>());
        when(orderService.findAllBySort(sortRequest)).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/sorting")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(sortRequest)))
                .andExpect(status().isOk());

    }


}
