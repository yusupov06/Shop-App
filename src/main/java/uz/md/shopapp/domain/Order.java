package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedBy;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.domain.template.AbsLongEntity;
import uz.md.shopapp.domain.template.AbsLongTimeStampEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE orders SET deleted = true where id = ?")
public class Order extends AbsLongTimeStampEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private User user;

    @LastModifiedBy
    private UUID updatedById;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.PREPARING;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    private Double overallPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
//        https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }


}
