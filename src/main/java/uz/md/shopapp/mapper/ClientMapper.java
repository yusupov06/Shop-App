package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.shopapp.domain.AccessKey;
import uz.md.shopapp.domain.Client;
import uz.md.shopapp.dtos.client.ClientAddDto;
import uz.md.shopapp.dtos.client.ClientDto;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {AccessKey.class, Collectors.class})
public interface ClientMapper extends EntityMapper<Client, ClientDto> {
    Client fromAddDto(ClientAddDto addDto);

    @Override
    @Mapping(target = "accessKeys", expression = " java( entity.getAccessKeys().stream().map(AccessKey::getAccess).collect(Collectors.toList()) ) ")
    ClientDto toDto(Client entity);

    @Override
    @Mapping(target = "accessKeys", ignore = true)
    Client fromDto(ClientDto dto);
}
