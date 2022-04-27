/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberMessageServiceImpl
 * Author:   Derek Xu
 * Date:     2022/2/17 11:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service.impl;

import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.boot.mapper.MemberMessageMapper;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/17
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MemberMessageServiceImpl extends BaseServiceImpl<MemberMessageMapper, MemberMessage> implements IMemberMessageService {


    @Override
    public List<MemberMessage> pages(String title, Long memberId, Integer page, Integer limit, Integer status) {
        return ((MemberMessageMapper) super.getBaseMapper()).pages(title, memberId, page, limit, status);
    }
}