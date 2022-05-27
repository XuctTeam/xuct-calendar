/**
 * Copyright (C), 2021-2022, 楚恬商行
 * FileName: FdfsProperties
 * Author:   Derek Xu
 * Date:     2022/5/27 16:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.fdfs.config;

import lombok.Data;
import org.csource.fastdfs.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2022/5/27
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("fastdfs")
public class FdfsProperties extends Properties {


}