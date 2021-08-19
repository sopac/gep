package org.sopac.gem.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.sopac.gem.IntegrationTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the CustomResource REST controller.
 *
 * @see CustomResource
 */
@IntegrationTest
class CustomResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        CustomResource customResource = new CustomResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(customResource).build();
    }

    /**
     * Test populate
     */
    @Test
    void testPopulate() throws Exception {
        restMockMvc.perform(get("/api/custom/populate")).andExpect(status().isOk());
    }

    /**
     * Test sachin
     */
    @Test
    void testSachin() throws Exception {
        restMockMvc.perform(get("/api/custom/sachin")).andExpect(status().isOk());
    }
}
