package budhioct.dev;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConnectionDBTest {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Test
    void testConnectionDB() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entityManager.close();
        entityManagerFactory.close();
    }

}
