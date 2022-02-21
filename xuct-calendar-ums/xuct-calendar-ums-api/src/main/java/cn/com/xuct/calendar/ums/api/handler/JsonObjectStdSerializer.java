/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: JsonObjectStdSerializer
 * Author:   Derek Xu
 * Date:     2022/2/21 14:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.json.JSONObject;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/21
 * @since 1.0.0
 */
@JsonComponent
public class JsonObjectStdSerializer extends StdSerializer<JSONObject> {

    protected JsonObjectStdSerializer() {
        super(JSONObject.class);
    }

    @Override
    public void serialize(JSONObject jsonObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(jsonObject.toString());
    }
}