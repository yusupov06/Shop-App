package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private Integer houseNumber;
    private String street;
    private String city;

    public Address(User user, Integer houseNumber, String street, String city) {
        this.user = user;
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return getId() != null && getId().equals(((Address) o).getId());
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
