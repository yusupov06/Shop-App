package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;

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

}
