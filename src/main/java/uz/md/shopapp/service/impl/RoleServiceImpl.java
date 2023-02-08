package uz.md.shopapp.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.role.RoleAddDTO;
import uz.md.shopapp.dtos.role.RoleDTO;
import uz.md.shopapp.dtos.role.RoleEditDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.RoleMapper;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.service.contract.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MessageSource messageSource;

    public RoleServiceImpl(RoleRepository roleRepository,
                           RoleMapper roleMapper,
                           MessageSource messageSource) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.messageSource = messageSource;
    }

    @Override
    public ApiResult<RoleDTO> add(RoleAddDTO dto) {
        if (roleRepository.existsByNameIgnoreCase(dto.getName()))
            throw new AlreadyExistsException(messageSource
                    .getMessage("ROLE_NAME_ALREADY_EXISTS", null, LocaleContextHolder.getLocale()));
        Role role = roleMapper.fromAddDTO(dto);
        return ApiResult
                .successResponse(roleMapper
                        .toDto(roleRepository.save(role)));
    }

    @Override
    public ApiResult<List<RoleDTO>> getAll() {
        return ApiResult
                .successResponse(roleMapper
                        .toDtoList(roleRepository
                                .findAll()));
    }

    @Override
    public ApiResult<RoleDTO> getById(Integer id) {
        return ApiResult
                .successResponse(roleMapper
                        .toDto(roleRepository
                                .findById(id)
                                .orElseThrow(() -> new NotFoundException(messageSource
                                        .getMessage("ROLE_NOT_FOUND_WITH_ID",
                                                null, LocaleContextHolder.getLocale()) + id))));
    }

    @Override
    public ApiResult<RoleDTO> edit(RoleEditDTO dto) {

        Role role = roleRepository
                .findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(messageSource
                        .getMessage("ROLE_NOT_FOUND_WITH_ID", null,
                                LocaleContextHolder.getLocale()) + dto.getId()));

        Role role1 = roleMapper.fromEditDto(role, dto);
        return ApiResult
                .successResponse(roleMapper
                        .toDto(roleRepository.save(role1)));
    }

    @Override
    public ApiResult<Boolean> delete(Integer id, Integer insteadOfRoleId) {
        return null;
    }
}
