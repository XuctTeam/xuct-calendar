/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthExceptionSerializer
 * Author:   Derek Xu
 * Date:     2021/11/23 9:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.core.exception;

import cn.com.xuct.calendar.common.core.enums.OAuth2ErrorEnum;
import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/23
 * @since 1.0.0
 */

public class AuthExceptionSerializer extends StdSerializer<Auth2Exception> {

    public AuthExceptionSerializer() {
        super(Auth2Exception.class);
    }

    @Override
    public void serialize(Auth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("code", OAuth2ErrorEnum.getCode(value.getMessage()));
        gen.writeStringField("msg", value.getErrorCode());
        gen.writeBooleanField("success", false);
        // 资源服务器会读取这个字段
        gen.writeStringField("error", value.getMessage());
        gen.writeEndObject();
    }
}