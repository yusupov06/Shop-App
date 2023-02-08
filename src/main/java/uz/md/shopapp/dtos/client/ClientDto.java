package uz.md.shopapp.dtos.client;

import lombok.*;
import uz.md.shopapp.domain.AccessKey;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClientDto {
    private Long id;
    private String username;
    private String phoneNumber;
    private List<String> accessKeys;
}
