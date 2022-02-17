/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberMessageMapper
 * Author:   Derek Xu
 * Date:     2022/2/17 11:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.mapper;

import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/17
 * @since 1.0.0
 */
public interface MemberMessageMapper extends BaseMapper<MemberMessage> {

    /**
     * 分页查询
     *
     * @param memberId
     * @param page
     * @param limit
     * @return
     */
    List<MemberMessage> pages(@Param("memberId") Long memberId, @Param("page") Integer page, @Param("limit") Integer limit);
}