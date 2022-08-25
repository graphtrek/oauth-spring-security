package co.grtk.oauth.client.repository;

import co.grtk.oauth.client.entity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<WebUser,Long> {
    WebUser findByEmail(String email);
}
