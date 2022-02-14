/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: FileUtils
 * Author:   Derek Xu
 * Date:     2022/2/13 10:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.web.utils;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/13
 * @since 1.0.0
 */
public class FileUtils {

    /**
     * 功能描述: <br>
     * 〈文件类型转换〉
     *
     * @param file
     * @param bh
     * @return:java.io.File
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/2/13 10:08
     */
    @SneakyThrows
    public static File multipartFileToFile(MultipartFile file, String bh, String suffix) {
        if (file.getSize() <= 0) {
            return null;
        }
        File toFile = null;
        // 用户主目录
        String userHome = System.getProperties().getProperty("user.home");
        StringBuilder filepath = new StringBuilder();
        filepath.append(userHome).append(File.separator).append("files").append(File.separator).append(bh).append(File.separator);
        //创建文件夹
        toFile = new File(filepath.toString());
        org.apache.commons.io.FileUtils.forceMkdir(toFile);
        //创建文件，此时文件为空
        String fileName = file.getOriginalFilename();
        if (fileName.lastIndexOf(".") > -1) {
            filepath.append(fileName);
        } else {
            filepath.append(fileName).append(".").append(suffix);
        }
        toFile = new File(filepath.toString());
        //为文件添加流信息
        file.transferTo(toFile);
        return toFile;
    }

    /**
     * 功能描述: <br>
     * 〈解决h5上传blob 没有文件后缀的问题〉
     *
     * @param contentType
     * @return:java.lang.String
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/2/13 15:29
     */
    public static String contentTypeToFileSuffix(String contentType) {
        if (contentType.startsWith("image")) {
            return contentType.split("/")[1];
        }
        return null;
    }

}