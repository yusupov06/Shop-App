package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.md.shopapp.domain.template.AbsUUIDEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")

@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users SET deleted = true where id = ?")
public class User extends AbsUUIDEntity implements UserDetails {

    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Address> addresses;

    @ManyToOne
    private Role role;

    public User(String firstName, String lastName, String phoneNumber, String password, Role role, boolean enabled) {
        super.setActive(true);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
//        https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(this.role.getPermissions());
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
