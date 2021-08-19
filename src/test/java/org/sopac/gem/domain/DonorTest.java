package org.sopac.gem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sopac.gem.web.rest.TestUtil;

class DonorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Donor.class);
        Donor donor1 = new Donor();
        donor1.setId(1L);
        Donor donor2 = new Donor();
        donor2.setId(donor1.getId());
        assertThat(donor1).isEqualTo(donor2);
        donor2.setId(2L);
        assertThat(donor1).isNotEqualTo(donor2);
        donor1.setId(null);
        assertThat(donor1).isNotEqualTo(donor2);
    }
}
