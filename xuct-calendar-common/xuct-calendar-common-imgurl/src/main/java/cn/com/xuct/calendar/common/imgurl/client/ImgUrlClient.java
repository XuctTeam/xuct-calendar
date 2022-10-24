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
package cn.com.xuct.calendar.common.imgurl.client;

import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.imgurl.config.ImgURLProperties;
import cn.com.xuct.calendar.common.imgurl.vo.ImgUrlReq;
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
public class ImgUrlClient {

    private final ImgURLProperties imgURLProperties;

    private final OkHttp3Fast okHttp3Fast;

    /**
     * 功能描述: <br>
     * 〈上传图片文件〉
     *
     * @param file
     * @return:cn.com.xuct.calendar.common.imgurl.vo.ImgUrlReq
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/2/13 9:25
     */
    @SneakyThrows
    public ImgUrlReq upload(File file) {

        MediaType mediaType = MediaType.Companion.parse("image/png");
        RequestBody fileBody = RequestBody.Companion.create(file, mediaType);
        Response response = okHttp3Fast.postMultipart(imgURLProperties.getUrl().concat("upload"), new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody).addFormDataPart("token", "").addFormDataPart("uid", "").build(), Headers.of(new HashMap<String, String>() {{
            put("Authorization", imgURLProperties.getToken());
            put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36 Edg/80.0.361.62");
        }}));
        if (response.isSuccessful()) {
            return JsonUtils.json2pojo(response.body().string(), ImgUrlReq.class);
        }
        return null;
    }
}