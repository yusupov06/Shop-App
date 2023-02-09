package uz.md.shopapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.AccessKey;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Client;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryInfoDto;
import uz.md.shopapp.dtos.client.ClientAddDto;
import uz.md.shopapp.dtos.client.ClientDto;
import uz.md.shopapp.dtos.product.ProductDto;
import uz.md.shopapp.exceptions.AccessKeyInvalidException;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.ClientMapper;
import uz.md.shopapp.repository.AccessKeyRepository;
import uz.md.shopapp.repository.CategoryRepository;
import uz.md.shopapp.repository.ClientRepository;
import uz.md.shopapp.service.contract.CategoryService;
import uz.md.shopapp.service.contract.ClientService;
import uz.md.shopapp.service.contract.ProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final AccessKeyRepository accessKeyRepository;
    private final ClientMapper clientMapper;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;

    @Override
    public ApiResult<List<CategoryInfoDto>> getAllCategories(String accessKey) {

        AccessKey accessKey1 = accessKeyRepository
                .findByAccess(accessKey)
                .orElseThrow(() -> new NotFoundException("ACCESS_KEY_NOT_FOUND"));

        if (!accessKey1.getValidTill().isAfter(LocalDateTime.now()))
            throw new AccessKeyInvalidException("ACCESS_KEY_IS_INVALID");

        return categoryService.getAllForInfo();
    }

    @Override
    public ApiResult<List<ProductDto>> getAllProductsByCategory(String accessKey, String categoryName) {
        AccessKey accessKey1 = accessKeyRepository
                .findByAccess(accessKey)
                .orElseThrow(() -> new NotFoundException("ACCESS_KEY_NOT_FOUND"));

        if (!accessKey1.getValidTill().isAfter(LocalDateTime.now()))
            throw new AccessKeyInvalidException("ACCESS_KEY_IS_INVALID");
        Category category = categoryRepository
                .findByName(categoryName)
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND"));
        return productService.getAllByCategory(category.getId());
    }

    @Override
    public ApiResult<ClientDto> getKey(ClientAddDto addDto) {
        if (clientRepository.existsByUsername(addDto.getUsername())) {
            throw new AlreadyExistsException("ACCESS_KEY_ALREADY_EXISTS");
        }

        Client client = clientMapper.fromAddDto(addDto);
        clientRepository.save(client);
        String generateString = RandomStringUtils.random(15, true, true);
        System.out.println("generateString = " + generateString);
        AccessKey save = accessKeyRepository.save(new AccessKey(generateString, client, 15));
        client.setAccessKeys(new ArrayList<>(List.of(save)));
        clientRepository.save(client);
        return ApiResult.successResponse(clientMapper.toDto(client));
    }
}
