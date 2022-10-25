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
import cn.com.xuct.calendar.common.imgurl.vo.ImgUrlRes;
import cn.com.xuct.calendar.common.imgurl.vo.data.ImgUrlData;
import com.github.chengtengfei.bean.OkHttp3Fast;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public ImgUrlData upload(File file) {

        MediaType mediaType = MediaType.Companion.parse("image/png");
        RequestBody fileBody = RequestBody.Companion.create(file, mediaType);
        Response response = okHttp3Fast.postMultipart(imgURLProperties.getPath(), new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//上传图片格式一般都是这个格式MediaType.parse("multipart/form-data")
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("token", imgURLProperties.getToken())
                .addFormDataPart("uid", imgURLProperties.getUid()).build(), Headers.of(new HashMap<String, String>() {{
            put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36 Edg/80.0.361.62");
        }}));
        if (response.isSuccessful()) {
            ImgUrlRes res = JsonUtils.json2pojo(response.body().string(), ImgUrlRes.class);
            if (res.getCode() == 200) {
                return JsonUtils.obj2pojo(res.getData(), ImgUrlData.class);
            }
            log.error("img url upload image:: code = {} , msg = {}", res.getCode(), res.getMsg());
        }
        return null;
    }
}