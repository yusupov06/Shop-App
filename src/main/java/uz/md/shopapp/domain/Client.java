package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.apache.tomcat.util.net.openssl.ciphers.Cipher;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    @ToString.Exclude
    private List<AccessKey> accessKeys = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
