package uz.md.shopapp.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AccessKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String access;

    private LocalDateTime validTill;
    private boolean deleted;
    @ManyToOne
    private Client client;

    public AccessKey(String access, Client client, int validTillInDays) {
        this.access = access;
        this.client = client;
        this.validTill = LocalDateTime.now().plusDays(validTillInDays);
    }
}
