/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: EnableAutoTencentSdk
 * Author:   Derek Xu
 * Date:     2021/11/29 16:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.tencent.annotation;

import cn.com.xuct.calendar.common.tencent.client.TencentSmsClient;
import cn.com.xuct.calendar.common.tencent.config.TencentConfiguration;
import cn.com.xuct.calendar.common.tencent.config.TencentProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/29
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(TencentProperties.class)
@Import({TencentConfiguration.class, TencentSmsClient.class})
public @interface EnableAutoTencentSdk {

}