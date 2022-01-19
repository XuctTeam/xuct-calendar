/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: EnableAutoLocalStoreCache
 * Author:   Derek Xu
 * Date:     2021/11/18 8:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.localstore.annotation;

import cn.com.xuct.calendar.common.localstore.config.CaffeineConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/18
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CaffeineConfiguration.class})
public @interface EnableAutoLocalStoreCache {

}