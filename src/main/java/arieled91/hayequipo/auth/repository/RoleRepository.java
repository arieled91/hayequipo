package arieled91.hayequipo.auth.repository;

import arieled91.hayequipo.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByName(String name);
}