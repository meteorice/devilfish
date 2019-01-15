package com.meteorice.devilfish.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {

    /**
     * 普通mapper
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 带顺序的mapper
     */
    private static final ObjectMapper orderMapper = new ObjectMapper();

    static {
        //map根据key排序
        orderMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        //开启字段排序
        orderMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        //------------------------->通用设置<-------------------------------

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        orderMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置输出时包含属性的风格
        orderMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }


    /**
     * Get json mapper object mapper.
     *
     * @return the object mapper
     */
    public static ObjectMapper getJsonMapper() {
        return mapper;
    }
}
