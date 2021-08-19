package org.sopac.gem.repository;

import java.util.List;
import java.util.Optional;
import org.sopac.gem.domain.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Proposal entity.
 */
@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    @Query(
        value = "select distinct proposal from Proposal proposal left join fetch proposal.resources left join fetch proposal.countries",
        countQuery = "select count(distinct proposal) from Proposal proposal"
    )
    Page<Proposal> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct proposal from Proposal proposal left join fetch proposal.resources left join fetch proposal.countries")
    List<Proposal> findAllWithEagerRelationships();

    @Query(
        "select proposal from Proposal proposal left join fetch proposal.resources left join fetch proposal.countries where proposal.id =:id"
    )
    Optional<Proposal> findOneWithEagerRelationships(@Param("id") Long id);
}
