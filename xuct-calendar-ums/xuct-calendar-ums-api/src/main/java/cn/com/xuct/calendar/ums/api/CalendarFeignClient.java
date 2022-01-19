/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: DavFeignClient
 * Author:   Derek Xu
 * Date:     2021/11/24 15:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api;


import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.dto.CalendarInitDto;
import cn.com.xuct.calendar.common.web.web.FeignConfiguration;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/24
 * @since 1.0.0
 */
@FeignClient(name = "dav-cms", contextId = "cms", configuration = FeignConfiguration.class)
public interface CalendarFeignClient {


    @PostMapping("/api/feign/v1/calendar")
    @Headers("Content-Type: application/json")
    R<String> addCarendar(CalendarInitDto calendarInitDto);

}

