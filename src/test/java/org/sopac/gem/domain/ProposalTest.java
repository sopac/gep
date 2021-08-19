package org.sopac.gem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sopac.gem.web.rest.TestUtil;

class ProposalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proposal.class);
        Proposal proposal1 = new Proposal();
        proposal1.setId(1L);
        Proposal proposal2 = new Proposal();
        proposal2.setId(proposal1.getId());
        assertThat(proposal1).isEqualTo(proposal2);
        proposal2.setId(2L);
        assertThat(proposal1).isNotEqualTo(proposal2);
        proposal1.setId(null);
        assertThat(proposal1).isNotEqualTo(proposal2);
    }
}
