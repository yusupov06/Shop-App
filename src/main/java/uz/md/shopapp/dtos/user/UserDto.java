package uz.md.shopapp.dtos.user;

import lombok.*;
import uz.md.shopapp.domain.enums.PermissionEnum;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isAdmin;
    private Set<PermissionEnum> permissions;
}
