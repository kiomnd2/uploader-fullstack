package com.kakaopay.uploader.util;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidateUtil {

    public boolean validData(String[] data) {
        if (data.length != 4) {
            return false;
        }
        for (String col : data) {
            if (StringUtils.isBlank(col)) {
                return false;
            }
        }
        return true;
    }
}
