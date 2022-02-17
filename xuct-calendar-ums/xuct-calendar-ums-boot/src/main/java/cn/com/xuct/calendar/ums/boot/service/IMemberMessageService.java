/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: IMemberMessageService
 * Author:   Derek Xu
 * Date:     2022/2/17 11:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service;

import cn.com.xuct.calendar.service.base.IBaseService;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.boot.mapper.MemberMessageMapper;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/17
 * @since 1.0.0
 */
public interface IMemberMessageService extends IBaseService<MemberMessageMapper, MemberMessage> {


    /**
     * 分页查询列表
     *
     * @param memberId
     * @param page
     * @param limit
     * @return
     */
    List<MemberMessage> pages(Long memberId, Integer page, Integer limit);
}