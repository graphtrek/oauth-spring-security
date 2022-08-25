package co.grtk.oauth.authserver.repository;


import co.grtk.oauth.authserver.entity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser,Long> {
    WebUser findByEmail(String email);
}
