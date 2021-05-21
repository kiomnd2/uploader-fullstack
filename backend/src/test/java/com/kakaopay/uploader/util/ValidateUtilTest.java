package com.kakaopay.uploader.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidateUtilTest {


    @Test
    void validateTest() throws Exception {
        String[] testStr = new String[]{"","","",""};
        assertThat(ValidateUtil.validData(testStr)).isFalse();

        String[] testStr2 = new String[]{"1","kim","hh"};
        assertThat(ValidateUtil.validData(testStr2)).isFalse();

    }

}
