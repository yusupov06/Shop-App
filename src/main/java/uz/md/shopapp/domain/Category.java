package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import uz.md.shopapp.domain.template.AbsLongEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE category SET deleted = true where id = ?")
public class Category extends AbsLongEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", fetch = FetchType.EAGER)
    @ToString.Include
    private List<Product> products;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }


}


