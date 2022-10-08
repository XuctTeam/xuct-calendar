/**
 * Copyright (C), 2021-2021, 263
 * FileName: EnableAutoSwagger2
 * Author:   Derek xu
 * Date:     2021/6/9 13:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.swagger.annotation;

import cn.com.xuct.calendar.common.swagger.config.SwaggerAutoConfiguration;
import cn.com.xuct.calendar.common.swagger.support.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/6/9
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({SwaggerAutoConfiguration.class})
public @interface EnableApiDoc {
}