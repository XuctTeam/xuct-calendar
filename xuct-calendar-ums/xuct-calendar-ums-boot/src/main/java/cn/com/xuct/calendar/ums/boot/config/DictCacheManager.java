/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: DictCacheManager
 * Author:   Derek Xu
 * Date:     2021/12/6 14:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.config;

import cn.com.xuct.calendar.dao.entity.Dict;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
@Slf4j
public class DictCacheManager {


    private static ConcurrentMap<String, List<Dict>> dictCache = Maps.newConcurrentMap();

    private DictCacheManager() {
    }

    private static class SingletonHolder {
        private static DictCacheManager INSTANCE = new DictCacheManager();
    }

    public static DictCacheManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static synchronized void init(List<Dict> dicts) {
        log.info("dict cache manager init...");
        Map<String, List<Dict>> maps = Maps.newHashMap();
        Dict dict = null;
        List<Dict> dic = null;
        for (int i = 0, j = dicts.size(); i < j; i++) {
            dict = dicts.get(i);
            dic = maps.get(dict.getType());
            if (dic == null) {
                dic = Lists.newArrayList();
                maps.put(dict.getType(), dic);
            }
            dic.add(dict);
        }
        DictCacheManager.dictCache.putAll(maps);
        log.info("dict cache manager init end...");
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param type
     * @param code
     * @return:cn.com.xuct.calendar.dao.entity.Dict
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2021/12/6 14:16
     */
    public static Dict getDictByCode(String type, String code) {
        List<Dict> dicts = DictCacheManager.dictCache.get(type);
        System.out.println(dictCache.get("time_zone"));
        if (CollectionUtils.isEmpty(dicts)) return null;
        Dict dict = dicts.stream().filter(x -> x.getCode().equals(code)).findFirst().orElse(new Dict());
        if (dict.getId() == null) return null;
        return dict;
    }
}