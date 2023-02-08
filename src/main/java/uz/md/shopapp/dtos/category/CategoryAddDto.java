package uz.md.shopapp.dtos.category;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryAddDto {

    @NotBlank(message = "Category name must not be empty")
    private String name;

    private String description;
}
