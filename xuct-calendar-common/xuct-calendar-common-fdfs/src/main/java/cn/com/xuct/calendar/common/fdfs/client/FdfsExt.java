/**
 * Copyright (C), 2015-2020, xuct.net
 * FileName: FastDFSExt
 * Author:   xutao
 * Date:     2020/5/16 13:04
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.fdfs.client;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author user
 * @create 2020/5/16
 * @since 1.0.0
 */
@Slf4j
public class FdfsExt {

    /**
     * ContentType
     */
    public static final Map<String, String> EXT_MAPS = new HashMap<>();


    private static boolean inited = false;


    synchronized static public void initExt() {
        if (!inited) {
            // image
            EXT_MAPS.put("png", "image/png");
            EXT_MAPS.put("gif", "image/gif");
            EXT_MAPS.put("bmp", "image/bmp");
            EXT_MAPS.put("ico", "image/x-ico");
            EXT_MAPS.put("jpeg", "image/jpeg");
            EXT_MAPS.put("jpg", "image/jpeg");
            // 压缩文件
            EXT_MAPS.put("zip", "application/zip");
            EXT_MAPS.put("rar", "application/x-rar");
            // doc
            EXT_MAPS.put("pdf", "application/pdf");
            EXT_MAPS.put("ppt", "application/vnd.ms-powerpoint");
            EXT_MAPS.put("xls", "application/vnd.ms-excel");
            EXT_MAPS.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            EXT_MAPS.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            EXT_MAPS.put("doc", "application/msword");
            EXT_MAPS.put("doc", "application/wps-office.doc");
            EXT_MAPS.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            EXT_MAPS.put("txt", "text/plain");
            // 音频
            EXT_MAPS.put("mp4", "video/mp4");
            EXT_MAPS.put("flv", "video/x-flv");
            inited = true;
        }
    }

}