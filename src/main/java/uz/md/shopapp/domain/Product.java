package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import uz.md.shopapp.domain.template.AbsLongEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@DynamicInsert
@DynamicUpdate
@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE product SET deleted = true where id = ?")
public class Product extends AbsLongEntity {

    @Column(nullable = false)
    private String name;

    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    public Product(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
//        https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }


}
