/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: DictCommandLineRunner
 * Author:   Derek Xu
 * Date:     2021/12/6 14:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.config;

import cn.com.xuct.calendar.ums.boot.service.IDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class DictCommandLineRunner implements CommandLineRunner {

    private final IDictService dictService;

    @Override
    public void run(String... args) throws Exception {
        DictCacheManager.init(dictService.list());
    }
}