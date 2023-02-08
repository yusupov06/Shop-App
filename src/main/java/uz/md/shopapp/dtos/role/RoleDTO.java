package uz.md.shopapp.dtos.role;

import lombok.*;
import uz.md.shopapp.domain.enums.PermissionEnum;

import java.util.Set;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RoleDTO {

    private Integer id;

    private String name;

    private String description;

    private Set<PermissionEnum> permissions;
}
