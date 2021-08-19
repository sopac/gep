package org.sopac.gem.repository;

import java.util.List;
import java.util.Optional;
import org.sopac.gem.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Project entity.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(
        value = "select distinct project from Project project left join fetch project.resources left join fetch project.countries",
        countQuery = "select count(distinct project) from Project project"
    )
    Page<Project> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct project from Project project left join fetch project.resources left join fetch project.countries")
    List<Project> findAllWithEagerRelationships();

    @Query("select project from Project project left join fetch project.resources left join fetch project.countries where project.id =:id")
    Optional<Project> findOneWithEagerRelationships(@Param("id") Long id);
}
