package pro.sky.sockswarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.sockswarehouse.entity.Socks;

import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long> {

    Optional<Socks> findSocksByColorAndCottonPart(String color, int cottonPart);

    @Query(value = "SELECT SUM(quantity) FROM socks s WHERE s.color = :color AND s.cotton_part < :cottonPart",
            nativeQuery = true)
    Optional<Integer> getQuantitySocksByColorAndCottonPartLessThan(@Param("color") String color,
                                                                   @Param("cottonPart") int cottonPart);

    @Query(value = "SELECT SUM(quantity) FROM socks s WHERE s.color = :color AND s.cotton_part = :cottonPart",
            nativeQuery = true)
    Optional<Integer> getQuantitySocksByColorAndCottonPartEqual(@Param("color") String color,
                                                                 @Param("cottonPart") int cottonPart);

    @Query(value = "SELECT SUM(quantity) FROM socks s WHERE s.color = :color AND s.cotton_part > :cottonPart",
            nativeQuery = true)
    Optional<Integer> getQuantitySocksByColorAndCottonPartMoreThan(@Param("color") String color,
                                                                   @Param("cottonPart") int cottonPart);
}
