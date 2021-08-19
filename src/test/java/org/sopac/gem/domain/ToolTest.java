package org.sopac.gem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sopac.gem.web.rest.TestUtil;

class ToolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tool.class);
        Tool tool1 = new Tool();
        tool1.setId(1L);
        Tool tool2 = new Tool();
        tool2.setId(tool1.getId());
        assertThat(tool1).isEqualTo(tool2);
        tool2.setId(2L);
        assertThat(tool1).isNotEqualTo(tool2);
        tool1.setId(null);
        assertThat(tool1).isNotEqualTo(tool2);
    }
}
