/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: SmmsClient
 * Author:   Derek Xu
 * Date:     2022/2/13 8:59
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.smms.client;

import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.smms.config.SmmsProperties;
import cn.com.xuct.calendar.common.smms.vo.SmmsRes;
import cn.com.xuct.calendar.common.smms.vo.SmmsUploadRes;
import com.github.chengtengfei.bean.OkHttp3Fast;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/13
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class SmmsClient {

    private final SmmsProperties smmsProperties;

    private final OkHttp3Fast okHttp3Fast;

    private static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");

    /**
     * 功能描述: <br>
     * 〈上传图片文件〉
     *
     * @param file
     * @return:cn.com.xuct.calendar.common.smms.vo.SmmsRes
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/2/13 9:25
     */
    @SneakyThrows
    public SmmsUploadRes upload(File file) {
        MultipartBody formBody = new MultipartBody.Builder().setType(FROM_DATA).addFormDataPart("smfile", file.getName(), RequestBody.create(MediaType.parse("image/png"), file)).build();
        Response response = okHttp3Fast.postMultipart(smmsProperties.getUrl().concat("upload"), formBody, Headers.of(new HashMap<String, String>() {{
            put("Authorization", smmsProperties.getToken());
            put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36 Edg/80.0.361.62");
        }}));
        if (response.isSuccessful()) {
            return JsonUtils.json2pojo(response.body().string(), SmmsUploadRes.class);
        }
        return null;
    }
}