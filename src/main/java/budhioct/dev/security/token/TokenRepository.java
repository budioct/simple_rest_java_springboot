package budhioct.dev.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findFirstByToken(String token);

    @Query(value = """
            select t from TokenEntity t inner join UserEntity u
            on t.user.id = u.id
            where u.id = :user_id and (t.expired = false or t.revoked = false)
            """)
    List<TokenEntity> findAllValidTokenByUser(@Param(value = "user_id" ) Long user_id);

}
