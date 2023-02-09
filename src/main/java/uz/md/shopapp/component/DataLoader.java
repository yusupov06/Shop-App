package uz.md.shopapp.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static uz.md.shopapp.domain.enums.PermissionEnum.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "simulation",
        name = "dataloader",
        havingValue = "true",
        matchIfMissing = true
)
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.firstName}")
    private String firstName;

    @Value("${app.admin.phoneNumber}")
    private String phoneNumber;

    @Value("${app.admin.password}")
    private String password;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String modeType;

    @Value("${app.running}")
    private String activeProfile;
    private List<Category> categories;
    private List<Product> products;
    private Role userRole;
    private List<User> users;
    private List<Address> addresses;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    @Override
    public void run(String... args) {
        System.out.println("activeProfile = " + activeProfile);
        if (Objects.equals("create", modeType)) {
            addAdmin();
            saveUserRole();
            addCategories();
            addProducts();
            addUsers();
            addAddresses();
        }
    }

    private void addAddresses() {
        ArrayList<Address> addresses = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            addresses.add(new Address(users.get(i), i, "street" + i, "city" + i));
        }

        this.addresses = addressRepository.saveAll(addresses);
    }

    private void addUsers() {

        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            users.add(new User("user" + i,
                    "user" + i,
                    "+99893" + String.valueOf(i + 1).repeat(7),
                    passwordEncoder.encode("user" + i + 1),
                    userRole,
                    true));
        }
        this.users = userRepository.saveAll(users);
    }

    private void addProducts() {
        products = productRepository.saveAll(
                new ArrayList<>(List.of(
                        new Product("Hp dx15y", " hp dx15y description ", 500.0, categories.get(0)),
                        new Product("Acer dx15y", " Acer dx15y description ", 500.0, categories.get(0)),
                        new Product("Lenovo dx15y", " Lenovo 15y description ", 500.0, categories.get(0)),
                        new Product("Monitor 128 ", " Monitor 128 description ", 500.0, categories.get(1)),
                        new Product(" Hp monitor fullHD  ", "  Hp monitor fullHD description ", 511.1, categories.get(1)),
                        new Product(" Kids toy  ", "  description ", 51.1, categories.get(2)),
                        new Product(" kids book  ", "  description ", 51.1, categories.get(2)),
                        new Product(" kids notebook  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Football ball  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Basketball ball  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Yasin TV  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Speed Read  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Deep work  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Spring boot in action  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Nexia  ", "  description ", 51.1, categories.get(2)),
                        new Product(" malibu  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Xiaomi 7  ", "  description ", 51.1, categories.get(2)),
                        new Product(" IPhone 12  ", "  description ", 51.1, categories.get(2)),
                        new Product(" IPhone 12 Pro  ", "  description ", 51.1, categories.get(2)),
                        new Product(" HP mono block  ", "  description ", 51.1, categories.get(2)),
                        new Product(" Apple  ", "  description ", 51.1, categories.get(2))
                ))
        );
    }


    private void addCategories() {
        categories = categoryRepository.saveAll(
                List.of(
                        new Category("all laptops", "laptops"),
                        new Category("all monitors", "monitors"),
                        new Category("all kids", "kids"),
                        new Category("all sport", "sport"),
                        new Category("all tvs", "tvs"),
                        new Category("all books", "books"),
                        new Category("all cars", "cars"),
                        new Category("all mobile phones", "mobile phones"),
                        new Category("all computers", "computers"),
                        new Category("all fruits", "fruits")
                )
        );
    }

    private void saveUserRole() {
        userRole = roleRepository.save(
                new Role("USER",
                        "System USER",
                        Set.of(GET_PRODUCT, GET_CATEGORY, GET_ORDER)
                )
        );
    }


    private void addAdmin() {
        userRepository.save(new User(
                firstName,
                "",
                phoneNumber,
                passwordEncoder.encode(password),
                addAdminRole(),
                true
        ));
    }

    private Role addAdminRole() {
        return roleRepository.save(
                new Role("ADMIN",
                        "System owner",
                        Set.of(PermissionEnum.values())
                )
        );
    }


}
