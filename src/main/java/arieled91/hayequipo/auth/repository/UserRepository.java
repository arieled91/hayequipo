package arieled91.hayequipo.auth.repository;


import arieled91.hayequipo.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
