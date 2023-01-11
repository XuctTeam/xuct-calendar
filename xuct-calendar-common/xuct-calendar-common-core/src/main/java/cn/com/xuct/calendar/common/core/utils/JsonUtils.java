/**
 * Copyright (C), 2021-2021, 263
 * FileName: JsonUtils
 * Author:   Derek xu
 * Date:     2021/6/17 21:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.utils;

import cn.com.xuct.calendar.common.core.constant.DateConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author user
 * @create 2020/5/23
 * @since 1.0.0
 */
public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = customObjectMapper(new ObjectMapper());

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }


    /**
     * 转换为 JSON 字符串
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String obj2json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换为 JSON 字符串，忽略空值
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String obj2jsonIgnoreNull(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换为 JavaBean
     *
     * @param jsonString
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T json2pojo(String jsonString, Class<T> clazz) {
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换为 Map<String, Object>
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    public static <T> Map<String, Object> json2map(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.readValue(jsonString, Map.class);
        } catch (Exception ee) {
            return null;
        }
    }

    /**
     * 字符串转换为 Map<String, T>
     */
    public static <T> Map<String, T> json2map(String jsonString, Class<T> clazz) {
        Map<String, Map<String, Object>> map = null;
        try {
            map = (Map<String, Map<String, Object>>) OBJECT_MAPPER.readValue(jsonString, new TypeReference<Map<String, T>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * 深度转换 JSON 成 Map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2mapDeeply(String json) {
        try {
            return json2MapRecursion(json, OBJECT_MAPPER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把 JSON 解析成 List，如果 List 内部的元素存在 jsonString，继续解析
     *
     * @param json
     * @param mapper 解析工具
     * @return
     * @throws Exception
     */
    private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) {
        if (json == null) {
            return null;
        }
        List<Object> list = null;
        try {
            list = mapper.readValue(json, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        for (Object obj : list) {
            if (obj != null && obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("[")) {
                    obj = json2ListRecursion(str, mapper);
                } else if (obj.toString().startsWith("{")) {
                    try {
                        obj = json2MapRecursion(str, mapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    /**
     * 把 JSON 解析成 Map，如果 Map 内部的 Value 存在 jsonString，继续解析
     *
     * @param json
     * @param mapper
     * @return
     * @throws Exception
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) {
        if (json == null) {
            return null;
        }
        Map<String, Object> map = null;
        try {
            map = mapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null && obj instanceof String) {
                String str = ((String) obj);
                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str, mapper);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }

        return map;
    }

    /**
     * 将 JSON 数组转换为集合
     *
     * @param jsonArrayStr
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> list = null;
        try {
            list = (List<T>) OBJECT_MAPPER.readValue(jsonArrayStr, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取泛型的 Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 将 Map 转换为 JavaBean
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    /**
     * 将 Map 转换为 JSON
     *
     * @param map
     * @return
     */
    public static String mapToJson(Map map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将 JSON 对象转换为 JavaBean
     *
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }

    /**
     * 判断字符串是否是JSON对象字符串
     *
     * @param str
     * @return boolean
     * @Date:2013-6-13
     * @author wangk
     * @Description:
     */
    public static boolean isJsonObjectString(String str) {
        return str != null && str.matches("^\\{.*\\}$");
    }

    /**
     * 判断字符串是否是JSON数组字符串
     *
     * @param str
     * @return boolean
     * @Date:2013-6-13
     * @author wangk
     * @Description:
     */
    public static boolean isJsonArrayString(String str) {
        return str != null && str.matches("^\\[.*\\]$");
    }


    /**
     * 自定义配置以增强 ObjectMapper 默认配置，主要体现在 Java8 时间处理、科学记数法处理
     * <br/>
     * 如果你还需要额外的增强配置可以这样使用：<br/>
     * <per>
     * final ObjectMapper OBJECT_MAPPER = JacksonUtils.customObjectMapper(new ObjectMapper());
     * OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("YYYY-MM-dd"));
     * </per>
     *
     * @param OBJECT_MAPPER the object mapper
     * @return the object mapper
     * @author leigq
     * @date 2020 -07-24 11:14:28
     */
    public static ObjectMapper customObjectMapper(ObjectMapper OBJECT_MAPPER) {
        OBJECT_MAPPER
                // 设置时区
                .setTimeZone(TimeZone.getTimeZone("GMT+8"))
                // Date 对象的格式，非 java8 时间
                .setDateFormat(new SimpleDateFormat(DateConstants.PATTERN_DATETIME))
                // 默认忽略值为 null 的属性，暂时不忽略，放开注释即不会序列化为 null 的属性
//				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 禁止打印时间为时间戳
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 禁止使用科学记数法
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);

        // 自定义Java8的时间兼容模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateConstants.DATETIME_FORMAT));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateConstants.DATE_FORMAT));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateConstants.TIME_FORMAT));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateConstants.DATETIME_FORMAT));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateConstants.DATE_FORMAT));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateConstants.TIME_FORMAT));
        /*注册模块*/
        OBJECT_MAPPER
                // Java8的时间兼容模块
                .registerModule(javaTimeModule)
                // Jdk8Module() -> 注册jdk8模块
                .registerModule(new Jdk8Module())
                // new ParameterNamesModule() ->
                .registerModule(new ParameterNamesModule());
        return OBJECT_MAPPER;
    }

}