package io.ecx.spring.graphql.webservice.repository;

import io.ecx.spring.graphql.webservice.models.PotresModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotresRepozitorij extends JpaRepository<PotresModel, Long> {

    boolean existsPotresModelById(final String id);

    PotresModel findPotresModelById(final String id);

    void deletePotresModelById(final String id);

    @Query(value = "SELECT * FROM potres p LIMIT :limit", nativeQuery = true)
    List<PotresModel> findPotresModelByLimit(@Param("limit") final Integer limit);

    List<PotresModel> findPotresModelsByTsunamiTrue();

    List<PotresModel> findPotresModelsByMagnitudaBetween(final Double minMagnituda, final Double maxMagnituda);
}
