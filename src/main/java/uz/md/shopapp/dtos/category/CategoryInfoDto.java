package uz.md.shopapp.dtos.category;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryInfoDto {
    private Long id;
    private String name;
    private String description;
}
