package org.sopac.gem.repository;

import org.sopac.gem.domain.Donor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Donor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {}
