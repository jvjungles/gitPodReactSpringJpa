package com.jungles.rsjapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jungles.rsjapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fera.class);
        Fera fera1 = new Fera();
        fera1.setId(1L);
        Fera fera2 = new Fera();
        fera2.setId(fera1.getId());
        assertThat(fera1).isEqualTo(fera2);
        fera2.setId(2L);
        assertThat(fera1).isNotEqualTo(fera2);
        fera1.setId(null);
        assertThat(fera1).isNotEqualTo(fera2);
    }
}
