package com.meteorice.devilfish.util;

import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StrUtils {

    /**
     * 获取url参数
     * @param query
     * @return
     */
    public static Map<String, String> analysisUrl(String query) {
        Map<String, String> params = new HashMap<>();
        String[] arrs = StringUtils.split(query, "&");
        for (int i = 0; i < arrs.length; i++) {
            String[] item = StringUtils.split(arrs[i], "=");
            if (item.length > 1) {
                params.put(item[0], item[1]);
            } else {
                params.put(item[0], null);
            }
        }
        return params;
    }
}
